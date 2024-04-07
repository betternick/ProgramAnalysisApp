package org.servlet;

import org.exception.CompilationException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.graph.*;
import org.profile.*;
import org.analysis.*;

import java.io.*;
import java.nio.file.*;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class Application {

    private static CFGBuilder cfg;
    private static Executor executor = Executor.getInstance();
    private static Analyser analyser;

    public static void main(String[] args) {
        // try {
        //     analyzeNewProject("examples/IfElse.java");
        // } catch (CompilationException e) {
        //     e.printStackTrace();
        // }

        System.out.println("Application, Start!");
        SpringApplication.run(Application.class, args);
        System.out.println("End of Application !");
    }

    public static void analyzeNewProject(String projectPath) throws CompilationException {
        cfg = new CFGBuilder();
        cfg.buildCFGs(projectPath);
        cfg.serializeMap("cfgMap.ser");

        cfg.printGlobalCFGMap();

        executor.execute(projectPath, "examples.IfElse", "cfgMap.ser", Log.getLogPath());

        String logPath = Log.getLogPath();
        analyser = new Analyser("cfgMap.ser");
        analyser.analyze(logPath);

        String json = analyser.getExecTreeAsJson();
        System.out.println(json);
    }
}
