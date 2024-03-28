package org.servlet;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.parser.*;
import org.graph.*;
import org.profile.*;
import org.analysis.*;

import java.io.File;

@SpringBootApplication
public class Application {

    private static CFGBuilder cfg;

    public static void main(String[] args) {
        analyzeNewProject("examples/Simple.java");

        // System.out.println("Application, Start!");
        // SpringApplication.run(Application.class, args);
        // System.out.println("End of Application !");
    }

    public static void analyzeNewProject(String projectPath) {
        cfg = new CFGBuilder();
        cfg.buildCFGs(projectPath);
        cfg.serializeMap("cfgMap.ser");

        Executor executor = Executor.getInstance();
        executor.execute(projectPath, "examples.Simple", "cfgMap.ser");

        String logPath = Log.getLogPath();
        Analyser analyser = Analyser.getInstance();
        analyser.analyze("cfgMap.ser", logPath);

        // Delete the log file
        File file = new File(logPath);
        if (file.delete()) {
            System.out.println("Log file deleted successfully");
        } else {
            System.out.println("Failed to delete the log file");
        }
    }
}
