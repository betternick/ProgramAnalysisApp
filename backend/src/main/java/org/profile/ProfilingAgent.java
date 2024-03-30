package org.profile;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

// Deserialization
import org.graph.*;
import java.io.*;
import java.util.Map;

public class ProfilingAgent {

    // Where is this agent compiled to?
    private static String agentPath = "build/libs/backend-agent.jar";

    public static String getAgentPath() {
        return agentPath;
    }

    private static String targetClassName;
    private static String targetMethodName;
    private static int targetLineNumber;

    private static Map<String, CFG> cfgMap;

    // We can't create the agent dynamically, so we need to read the file passed
    // from graph-generation module
    private static void parseAgentArguments(String graphPath) {
        cfgMap = ProfilingAgent.deserializeMap(graphPath);
        targetLineNumber = 3;
    }

    private static Map<String, CFG> deserializeMap(String fileName) {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Map<String, CFG>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        } catch (ClassCastException e) {
            System.err.println("Error casting to Map<String, CFG>: " + e.getMessage());
        }
        return null; // Return null if deserialization failed
    }

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
                // Ensure we only transform our target classes to avoid unnecessary processing
                if (!className.replace("/", ".").contains("examples.Simple")) {
                    return null;
                }
                System.out.println("Class Name: " + className);

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
        private CFG cfg;

        protected MethodAdapter(int api, MethodVisitor mv, int access, String name, String desc, String className) {
            super(api, mv, access, name, desc);
            this.className = className;
            this.methodName = name;

            String transformed = transformNameAndDescToCfgFormat(name, desc);
            // Get the corresponding CFG for the method
            CFG functionCFG = cfgMap.get(transformed);
            if (functionCFG == null) {
                // Do nothing if the CFG is not found
                return;
            }
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            super.visitLineNumber(line, start);
            // if (line == targetLineNumber) {
            injectAction(line);
            // }
        }

        private void injectAction(int line) {
            String identifier = className + "." + methodName + ":" + line;

            // Call MonitoringService.logTime
            mv.visitLdcInsn(identifier);
            mv.visitMethodInsn(INVOKESTATIC, "org/profile/Log", "logNormalInfo", "(Ljava/lang/String;)V", false);
        }
    }
}
