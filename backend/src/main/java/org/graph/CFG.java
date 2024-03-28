package org.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CFG implements Serializable {
    private List<Node> nodes = new ArrayList<>();
    private Node entryNode;
    private Node exitNode;

    public Node createNode(CodeBlock codeBlock) {
        Node node = new Node(codeBlock);
        nodes.add(node);
        return node;
    }

    public void setEntryNode(Node node) {
        entryNode = node;
    }

    public void setExitNode(Node node) {
        exitNode = node;
    }

    public Node getExitNode() {
        return this.exitNode;
    }

    public void print() {
        for (Node node : nodes) {
            System.out.println(node);
            for (Node successor : node.next) {
                System.out.println("Child -> " + successor);
            }
        }
    }

    public String toTextRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            sb.append(node.codeBlock.toString()).append(" (ID: ").append(node.id).append(")\n");
            for (Node successor : node.next) {
                sb.append("  |---> ").append(successor.codeBlock.toString()).append(" (ID: ").append(successor.id)
                        .append(")\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
