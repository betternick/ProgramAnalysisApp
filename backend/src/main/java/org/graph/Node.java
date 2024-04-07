package org.graph;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

public class Node implements Serializable {
    public static int nextId = 0; // Static variable to generate unique IDs
    public int id; // Unique ID for each node
    public CodeBlock codeBlock;
    public List<Node> prev = new ArrayList<>();
    public List<Node> next = new ArrayList<>();
    public List<String> comments = new ArrayList<>(); // List to store comments

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

    public List<Node> getNext() {
        return next;
    }

    public int getLineStart() {
        return codeBlock.getLineStart();
    }

    public int getId() {
        return id;
    }

    public void addComment(String comment) {
        if (!comments.contains(comment)) {
            comments.add(comment);
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", codeBlock=" + codeBlock +
                ", comments=" + comments +
                '}';
    }
}
