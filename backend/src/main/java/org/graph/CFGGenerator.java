//package org.graph;
//
//import soot.*;
//import soot.options.Options;
//import soot.toolkits.graph.ExceptionalUnitGraph;
//import soot.toolkits.graph.UnitGraph;
//
//import java.util.List;
//
//public class CFGGenerator {
//    public static void main(String[] args) {
//        // Set up Soot
//        G.reset();
//        Options.v().set_prepend_classpath(true);
//        Options.v().set_src_prec(Options.src_prec_java);
//        Options.v().set_process_dir(List.of("backend/build/classes/java/main")); // Set the path to your classes
//        Options.v().set_output_format(Options.output_format_none);
//        Scene.v().loadNecessaryClasses();
//
//        // Load the class and method
//        SootClass sootClass = Scene.v().loadClassAndSupport("org.graph.CFGBuilder");
//        sootClass.setApplicationClass();
//        SootMethod method = sootClass.getMethodByName("main");
//
//        // Create the CFG
//        Body body = method.retrieveActiveBody();
//        UnitGraph cfg = new ExceptionalUnitGraph(body);
//
//        // Print the CFG (or perform other analyses)
//        for (Unit unit : cfg) {
//            System.out.println(unit);
//            for (Unit succ : cfg.getSuccsOf(unit)) {
//                System.out.println("  -> " + succ);
//            }
//        }
//    }
//}
