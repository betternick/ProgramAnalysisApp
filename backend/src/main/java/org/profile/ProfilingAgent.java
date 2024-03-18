package org.profile;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import java.lang.instrument.Instrumentation;

public class ProfilingAgent {

    public static void premain(String arguments, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .type(ElementMatchers.nameContains("Simple"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) -> builder
                        .method(ElementMatchers.any())
                        .intercept(Advice.to(TimingAdvice.class)))
                .installOn(instrumentation);
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
