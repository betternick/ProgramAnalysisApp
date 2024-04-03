package org.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CFG implements Serializable {
    public List<Node> nodes = new ArrayList<>();
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

    // Traverse all nodes
    public List<Integer> getAllLines() {
        List<Integer> lines = new ArrayList<>();
        return lines;
    }

    public void print() {
        for (Node node : nodes) {
            System.out.println(node);
            for (Node successor : node.next) {
                System.out.println("Child -> " + successor);
            }
        }
    }

    public List<Node> getNodes() {
        // returns an unmodifiable view of the nodes list
        return Collections.unmodifiableList(nodes);
    }

    public Node getEntryNode() {
        return this.entryNode;
    }

    // public String toTextRepresentation() {
    // StringBuilder sb = new StringBuilder();
    // for (Node node : nodes) {
    // sb.append(node.codeBlock.toString()).append(" (ID:
    // ").append(node.id).append(")\n");
    // for (Node successor : node.next) {
    // sb.append(" |---> ").append(successor.codeBlock.toString()).append(" (ID:
    // ").append(successor.id)
    // .append(")\n");
    // }
    // sb.append("\n");
    // }
    // return sb.toString();
    // }

    public String toTextRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            sb.append(node.codeBlock.toString()).append(" (ID: ").append(node.id);
            sb.append(", Comments: ").append(node.comments);
            sb.append(")\n");
            for (Node successor : node.next) {
                sb.append("  |---> ").append(successor.codeBlock.toString()).append(" (ID: ").append(successor.id);
                sb.append(", Comments: ").append(successor.comments);
                sb.append(")\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
