// package org.graph;
//
// import soot.*;
// import soot.options.Options;
//
// import java.util.List;
//
// public class SimpleSootExample {
// public static void main(String[] args) {
// // Initialize Soot
// G.reset();
// Options.v().set_prepend_classpath(true);
// Options.v().set_src_prec(Options.src_prec_java);
// Options.v().set_process_dir(List.of("backend/build/classes/java/main")); //
// Adjust this path to where your compiled classes are
// Options.v().set_output_format(Options.output_format_none);
// Scene.v().loadNecessaryClasses();
//
// // Load the class
// SootClass sootClass = Scene.v().loadClassAndSupport("CFG");
// sootClass.setApplicationClass();
//
// // Print the methods of the class
// System.out.println("Methods of " + sootClass.getName() + ":");
// for (SootMethod method : sootClass.getMethods()) {
// System.out.println(" " + method.getSignature());
// }
// }
// }
//
