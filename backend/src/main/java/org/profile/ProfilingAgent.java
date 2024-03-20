package org.profile;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import net.bytebuddy.asm.Advice;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

public class ProfilingAgent {

    private static String targetClassName;
    private static String targetMethodName;
    private static int targetLineNumber;

    public static void premain(String arguments, Instrumentation inst) {
        // new AgentBuilder.Default()
        // .type(ElementMatchers.nameContains("Simple"))
        // .transform((builder, typeDescription, classLoader, module, protectionDomain)
        // -> builder
        // .method(ElementMatchers.any())
        // .intercept(Advice.to(TimingAdvice.class)))
        // .installOn(inst);

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                if (className == null) {
                    return null; // Primitive types do not have a class loader, class name, etc.
                }

                // Ensure we only transform our target classes to avoid unnecessary processing
                // if (!className.replace("/", ".").equals("org.profile.ProfilingAgent")) {
                // return null;
                // }

                try {
                    ClassReader reader = new ClassReader(classfileBuffer);
                    ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    ClassVisitor visitor = new ClassAdapter(writer);
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
        public ClassAdapter(final ClassVisitor cv) {
            super(Opcodes.ASM9, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

            // System.out.println("Access: " + access + " Name: " + name + " Desc: " + desc
            // + " Signature: " + signature + " Exceptions: " + exceptions);
            return new MethodAdapter(Opcodes.ASM9, mv, access, name, desc);
        }
    }

    static class MethodAdapter extends AdviceAdapter {
        private boolean injectHere = false;

        protected MethodAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            // System.out.println("Line: " + line + " Start: " + start);
            super.visitLineNumber(line, start);
            if (line == targetLineNumber) {
                injectHere = true;
            }
        }

        @Override
        public void visitInsn(int opcode) {
            // System.out.println("opcode: " + opcode);
            if (injectHere) {
                // Inject the action here, e.g., logging
                injectAction();
                injectHere = false; // Ensure action is injected only once per target line
            }
            super.visitInsn(opcode);
        }

        @Override
        protected void onMethodEnter() {
            // System.out.println("Method Enter");
            // Optional: code to execute at the beginning of a method
        }

        @Override
        protected void onMethodExit(int opcode) {
            // System.out.println("Method Exit");
            // Optional: code to execute at the end of a method
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            // System.out.println("Jump Instruction" + opcode + label);
            // Here, you inspect the opcode to determine if it represents a conditional
            // branch.
            // If so, insert your logging or counting logic here.
            super.visitJumpInsn(opcode, label);
        }

        private void injectAction() {
            // Example action: Print to System.out
            System.out.println("Here");
            // mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
            // "Ljava/io/PrintStream;");
            // mv.visitLdcInsn("Executing line " + targetLineNumber);
            // mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
            // "(Ljava/lang/String;)V", false);
        }
    }

    private static void parseAgentArguments(String agentArgs) {
        // Parse the agentArgs string to extract className, methodName, and lineNumber
        targetClassName = "Simple";
        targetMethodName = "main";
        targetLineNumber = 1; // Example line number
    }

    public static class TimingAdvice {
        @Advice.OnMethodEnter
        static long enter() {
            return System.currentTimeMillis();
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class)
        static void exit(@Advice.Origin String method, @Advice.Enter long startTime) {
            long endTime = System.currentTimeMillis();
            System.out.println(method + " took " + (endTime - startTime) + "ms");
        }
    }
}
