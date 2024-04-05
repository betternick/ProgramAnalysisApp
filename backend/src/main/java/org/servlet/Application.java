package org.servlet;

import org.exception.CompilationException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

import org.graph.*;
import org.profile.*;
import org.analysis.*;

import java.io.*;
import java.nio.file.*;

@SpringBootApplication
public class Application {

    private static CFGBuilder cfg;
    private static Executor executor = Executor.getInstance();
    private static Analyser analyser;

    public static void main(String[] args) {
        // analyzeNewProject("examples/ShouldPass.java");

        System.out.println("Application, Start!");
        SpringApplication.run(Application.class, args);
        System.out.println("End of Application !");
    }

    public static void analyzeNewProject(String projectPath) throws CompilationException {
        cfg = new CFGBuilder();
        cfg.buildCFGs(projectPath);
        cfg.serializeMap("cfgMap.ser");

        executor.execute(projectPath, "examples.ShouldPass", "cfgMap.ser", Log.getLogPath());

        String logPath = Log.getLogPath();
        analyser = new Analyser("cfgMap.ser");
        analyser.analyze(logPath);
    }
}
