package org.graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public CodeBlock codeBlock;
    public List<Node> prev = new ArrayList<>();
    public List<Node> next = new ArrayList<>();

    public Node(CodeBlock codeBlock) {
        this.codeBlock = codeBlock;
    }

    public void addPrevious(Node node) {
        prev.add(node);
    }

    public void addNext(Node node) {
        next.add(node);
    }

    @Override
    public String toString() {
        return "Node: " + codeBlock.toString();
    }
}
