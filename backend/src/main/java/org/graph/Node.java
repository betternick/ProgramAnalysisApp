package org.graph;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public static int nextId = 0; // Static variable to generate unique IDs
    public int id; // Unique ID for each node
    public CodeBlock codeBlock;
    public List<Node> prev = new ArrayList<>();
    public List<Node> next = new ArrayList<>();

    public Node(CodeBlock codeBlock) {
        this.id = nextId++; // Assign a unique ID and increment the counter
        this.codeBlock = codeBlock;
    }

    public void addPrevious(Node node) {
        prev.add(node);
    }

    public void addNext(Node node) {
        next.add(node);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", codeBlock=" + codeBlock +
                '}';
    }
}
