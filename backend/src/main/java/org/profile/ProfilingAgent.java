package org.profile;

// Instrumentation
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import org.graph.*;
import java.io.*;
import java.util.*;
import org.apache.commons.lang3.tuple.Pair;

public class ProfilingAgent {

    // Where is this agent compiled to?
    private static String agentPath = "build/libs/backend-agent.jar";

    public static String getAgentPath() {
        return agentPath;
    }

    // The map of functions names to their CFGs
    private static Map<String, CFG> cfgMap;

    private static List<String> classToInstrument;

    // We can't create the agent dynamically, so we need to read the file passed
    // from graph-generation module
    private static void parseAgentArguments(String graphPathAndClass) {
        // The argument is in the format "graphPath:class1:class2:class3"
        // The graphPath is compulsory, the classes are optional
        String[] parts = graphPathAndClass.split(":");
        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid agent arguments" + graphPathAndClass);
        }
        cfgMap = CFGBuilder.deserializeMap(parts[0]);
        classToInstrument = Arrays.asList(parts).subList(1, parts.length);
    }

    // The method description is different from the CFG format, we need to convert
    // e.g. (I)V to int
    // e.g. (Ljava/lang/String;I)V to java.lang.String,int
    public static String transformNameAndDescToCfgFormat(String name, String desc) {
        StringBuilder result = new StringBuilder(name);

        // Extract parameter descriptors from the method descriptor
        String paramsDesc = desc.substring(desc.indexOf('(') + 1, desc.indexOf(')'));

        result.append("(");

        // Initialize variables for parsing
        int arrayDepth = 0;
        boolean isObjectType = false;
        StringBuilder paramType = new StringBuilder();

        for (int i = 0; i < paramsDesc.length(); i++) {
            char ch = paramsDesc.charAt(i);

            switch (ch) {
                case '[':
                    arrayDepth++;
                    break;
                case 'L':
                    isObjectType = true;
                    break;
                case ';':
                    if (isObjectType) {
                        // Append the object type name (simplified)
                        String typeName = paramType.toString().replace('/', '.');
                        // typeName = simplifyTypeName(typeName);
                        result.append(typeName);
                        if (arrayDepth > 0) {
                            result.append("[]".repeat(arrayDepth));
                        }
                        result.append(",");
                        // Reset for the next parameter
                        paramType = new StringBuilder();
                        arrayDepth = 0;
                        isObjectType = false;
                    }
                    break;
                default:
                    if (isObjectType) {
                        paramType.append(ch);
                    } else {
                        // Handle primitive types and their arrays
                        String typeName = getPrimitiveTypeName(ch);
                        result.append(typeName);
                        if (arrayDepth > 0) {
                            result.append("[]".repeat(arrayDepth));
                            arrayDepth = 0;
                        }
                        result.append(",");
                    }
                    break;
            }
        }

        // Remove trailing comma and space if any
        if (result.charAt(result.length() - 1) == ',') {
            result.delete(result.length() - 1, result.length());
        }

        result.append(")");
        return result.toString();
    }

    private static String getPrimitiveTypeName(char descriptor) {
        return switch (descriptor) {
            case 'B' -> "byte";
            case 'C' -> "char";
            case 'D' -> "double";
            case 'F' -> "float";
            case 'I' -> "int";
            case 'J' -> "long";
            case 'S' -> "short";
            case 'Z' -> "boolean";
            default -> "";
        };
    }

    public static void premain(String arguments, Instrumentation inst) {
        // Set what class and method to target
        parseAgentArguments(arguments);

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain, byte[] classfileBuffer) {

                if (className == null) {
                    return null; // Primitive types do not have a class loader, class name, etc.
                }
                // Ensure we only transform our target classes (claaToInstrument)
                if (!classToInstrument.contains(className.replace("/", "."))) {
                    return null;
                }

                try {
                    ClassReader reader = new ClassReader(classfileBuffer);
                    ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    ClassVisitor visitor = new ClassAdapter(writer, className);
                    reader.accept(visitor, ClassReader.EXPAND_FRAMES);
                    return writer.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });

    }

    static class ClassAdapter extends ClassVisitor {
        private String className;

        public ClassAdapter(final ClassVisitor cv, String className) {
            super(Opcodes.ASM9, cv);
            this.className = className;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            return new MethodAdapter(Opcodes.ASM9, mv, access, name, desc, className);
        }
    }

    static class MethodAdapter extends AdviceAdapter {
        private String className;
        private String methodName;
        private String cfgName;
        private CFG cfg;

        private List<Pair<Integer, Integer>> lineAndId = new ArrayList<>();

        protected MethodAdapter(int api, MethodVisitor mv, int access, String name, String desc, String className) {
            super(api, mv, access, name, desc);
            this.className = className;
            this.methodName = name;

            cfgName = transformNameAndDescToCfgFormat(name, desc);
            // Get the corresponding CFG for the method
            CFG functionCFG = cfgMap.get(cfgName);
            if (functionCFG == null) {
                // Do nothing if the CFG is not found
                return;
            }

            collectInjectPlaces(functionCFG);
        }

        private void collectInjectPlaces(CFG cfg) {
            for (Node node : cfg.nodes) {
                lineAndId.add(node.getLineAndId());
            }
        }

        private Optional<Integer> getIdForLine(int lineNumber) {
            for (Pair<Integer, Integer> pair : lineAndId) {
                if (pair.getLeft().equals(lineNumber)) {
                    return Optional.of(pair.getRight());
                }
            }
            return Optional.empty();
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            super.visitLineNumber(line, start);

            Optional<Integer> id = getIdForLine(line);

            if (id.isPresent()) {
                injectAction(id.get());
            }
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();
            // Print a message upon entering the method
            mv.visitLdcInsn(cfgName);
            mv.visitMethodInsn(INVOKESTATIC, "org/profile/Log", "enter", "(Ljava/lang/String;)V", false);
        }

        protected void onMethodExit(int opcode) {
            // We don't handle exception exits,
            // if (opcode != ATHROW) {
            mv.visitLdcInsn(cfgName);
            mv.visitMethodInsn(INVOKESTATIC, "org/profile/Log", "exit", "(Ljava/lang/String;)V", false);

            super.onMethodExit(opcode);
        }

        private void injectAction(int nodeID) {
            String identifier = String.valueOf(nodeID);

            // Call MonitoringService.logTime
            mv.visitLdcInsn(identifier);
            mv.visitMethodInsn(INVOKESTATIC, "org/profile/Log", "logNormalInfo", "(Ljava/lang/String;)V", false);
        }

    }
}
