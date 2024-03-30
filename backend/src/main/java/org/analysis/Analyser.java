package org.analysis;

public class Analyser {
    private static Analyser instance = null;

    private Analyser() {
    }

    public static synchronized Analyser getInstance() {
        if (instance == null) {
            instance = new Analyser();
        }
        return instance;
    }

    public void analyze(String graphPath, String logPath) {
        System.out.println("Analyzing the project" + graphPath + logPath);
        // Read the graph from the file
        // Analyze the graph
    }
}
