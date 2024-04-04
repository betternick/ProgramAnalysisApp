package org.analysis;

import org.graph.*;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;

public class ExecTree {
    // What codeNode this one represents
    private Node codeNode;

    // If there is no child, it means there is no function call
    private List<ExecTree> children;

    // We can't get the execution time at the beginning, so we need to set it later
    private long startTimeInNano;
    // Execution time in nanoseconds, this field is valid after calling
    // calculateExecutionTimes()
    private int executionTimeInNano;

    private long memoryUsage;
    private float cpuUsage;

    public void setExecutionTimeInNano(long endTimeInNano) {
        this.executionTimeInNano = (int) (endTimeInNano - startTimeInNano);
    }

    public int getExecutionTimeInNano() {
        return executionTimeInNano;
    }

    // Method to calculate execution times recursively
    public void calculateExecutionTimes(long endTimeInNano) {
        // If this is a leaf node, the execution time is the difference between end and
        // start time
        setExecutionTimeInNano(endTimeInNano);
        if (!isLeaf()) {
            // Children are ordered according to their execution start time
            // The end time for a child is the start time of its next sibling
            // The end time of the last child is the end time of the parent node
            for (int i = 0; i < children.size(); i++) {
                long childEndTime = (i < children.size() - 1) ? children.get(i + 1).startTimeInNano : endTimeInNano;
                children.get(i).calculateExecutionTimes(childEndTime);
            }
        }
    }

    public Node getCodeNode() {
        return codeNode;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public float getCpuUsage() {
        return cpuUsage;
    }

    public ExecTree(Node codeNode, long startTimeInNano, long memoryUsage, float cpuUsage) {
        this.codeNode = codeNode;
        this.startTimeInNano = startTimeInNano;
        this.memoryUsage = memoryUsage;
        this.cpuUsage = cpuUsage;
        children = new ArrayList<>();
    }

    // Means this node contains no function call
    public boolean isLeaf() {
        return children.isEmpty();
    }

    public void addChild(ExecTree child) {
        children.add(child);
    }

    public List<ExecTree> getChildren() {
        return children;
    }

    // Convert this ExecTree to a JSON object
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        if (this.getCodeNode() != null) {
            json.put("id", this.getCodeNode().getId());
        }
        json.put("startTimeInNano", this.startTimeInNano);
        json.put("executionTimeInNano", this.executionTimeInNano);
        json.put("memoryUsage", this.memoryUsage);
        json.put("cpuUsage", this.cpuUsage);

        // If this node has children, add them as a JSON array
        if (!isLeaf()) {
            JSONArray childrenJSON = new JSONArray();
            for (ExecTree child : getChildren()) {
                childrenJSON.put(child.toJSON()); // Recursive call
            }
            json.put("children", childrenJSON);
        }

        return json;
    }
}
