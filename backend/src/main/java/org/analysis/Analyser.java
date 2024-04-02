package org.analysis;

import org.graph.*;

import java.util.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;

public class Analyser {
    // The map of all CFGs
    private Map<String, CFG> cfgMap;
    // The root of the execution tree
    private ExecTree root = null;

    public Analyser(String graphPath) {
        cfgMap = CFGBuilder.deserializeMap(graphPath);
    }

    // Each CFG contains a list of nodes, and each node has an ID
    private Node getNodefromId(int id) {
        for (CFG cfg : cfgMap.values()) {
            for (Node node : cfg.getNodes()) {
                if (node.getId() == id) {
                    return node;
                }
            }
        }
        return null;
    }

    // Helper class to store information about each log line
    private class Info {
        Node node;
        long time;
        long memory;
        float cpu;

        public Info(Node node, long time, long memory, float cpu) {
            this.node = node;
            this.time = time;
            this.memory = memory;
            this.cpu = cpu;
        }
    }

    // For line with format "id:time:memory:cpu"
    private Info parseLine(String line) {
        String[] parts = line.split(":");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid line format  " + line);
        }

        try {
            int id = Integer.parseInt(parts[0]);
            long time = Long.parseLong(parts[2]);
            long memory = Long.parseLong(parts[4]);
            float cpu = Float.parseFloat(parts[6]);

            // Find corresponding node
            Node node = getNodefromId(id);
            if (node == null) {
                throw new IllegalArgumentException("Node with id " + id + " not found");
            }

            return new Info(node, time, memory, cpu);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e + "\nInvalid line format  " + line);
        }
    }

    // We build the execution tree
    private void traverse(BufferedReader reader) throws IOException {
        Stack<ExecTree> execStack = new Stack<>();

        String line = reader.readLine();
        String[] parts = line.split(":");

        if (!parts[0].equals("Enter") || !parts[1].equals("main(java.lang.String[])")) {
            throw new IllegalArgumentException(
                    "The first line of the log file should be Enter:main(java.lang.String[]), not: " + line);
        }

        long startTimeInNano = Long.parseLong(parts[2]);
        root = new ExecTree(null, startTimeInNano, 0, 0);
        ExecTree currExecNode = root;

        // It assumes the last log line is "Exit:main(java.lang.String[])"
        while (parts[0].equals("Exit") && parts[1].equals("main(java.lang.String[])")) {

            if (line.startsWith("Enter:")) {
                execStack.push(currExecNode);

            } else if (line.startsWith("Exit:")) {
                execStack.pop();

            } else {
                ExecTree parentNode = execStack.peek();
                Info currInfo = parseLine(line);
                currExecNode = new ExecTree(currInfo.node, currInfo.time, currInfo.memory, currInfo.cpu);

                parentNode.addChild(currExecNode);
            }
            System.out.println(line);

            line = reader.readLine();
            parts = line.split(":");
        }

        long endTimeInNano = Long.parseLong(parts[2]);
        assert (currExecNode == root);
        // This will calculate the execution times for all nodes recursively
        currExecNode.calculateExecutionTimes(endTimeInNano);
    }

    // In the second traverse, we update the statistics
    private void secondTraverse(ExecTree root) {
        for (ExecTree child : root.getChildren()) {
            System.out.println(child);
            secondTraverse(child);
        }
    }

    public void analyze(String logPath) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(logPath))) {
            // Traverse the log file and build the execution tree
            traverse(reader);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
            return;
        }
    }

    // Helper method to recursively find all nodes with the given ID
    private void findNodesById(ExecTree currentNode, int id, List<ExecTree> matchingNodes) {
        if (currentNode.getCodeNode() != null && currentNode.getCodeNode().getId() == id) {
            matchingNodes.add(currentNode);
        }
        for (ExecTree child : currentNode.getChildren()) {
            findNodesById(child, id, matchingNodes);
        }
    }

    // Get statistics for a given node ID
    public ExecTreeStats getStat(int id) {
        List<ExecTree> matchingNodes = new ArrayList<>();
        findNodesById(root, id, matchingNodes);

        // Calculate statistics
        int totalExecutionTime = 0;
        long totalMemoryUsage = 0;
        float totalCpuUsage = 0;

        for (ExecTree node : matchingNodes) {
            totalExecutionTime += node.getExecutionTimeInNano();
            totalMemoryUsage += node.getMemoryUsage();
            totalCpuUsage += node.getCpuUsage();
        }

        int executionTimes = matchingNodes.size();
        double averageExecutionTime = executionTimes > 0 ? (double) totalExecutionTime / executionTimes : 0;
        double averageMemoryUsage = executionTimes > 0 ? (double) totalMemoryUsage / executionTimes : 0;
        double averageCpuUsage = executionTimes > 0 ? (double) totalCpuUsage / executionTimes : 0;

        return new ExecTreeStats(executionTimes, averageExecutionTime, averageMemoryUsage, averageCpuUsage);
    }
}
