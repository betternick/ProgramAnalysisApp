package org.graph;

class CodeBlock {
    public String[] code;
    public String fileName;
    public int lineStart;

    public CodeBlock(String[] code, String fileName, int lineStart) {
        this.code = code;
        this.fileName = fileName;
        this.lineStart = lineStart;
    }
}

public class Node {
    // The corresponding code block
    public CodeBlock codeBlock;
    // Link to previous node
    public Node[] prev;
    // Link to next node
    public Node[] next;

    // Node constructor
    public Node(CodeBlock codeBlock, Node[] prev, Node[] next) {
        this.codeBlock = codeBlock;
        this.prev = prev;
        this.next = next;
    }
}
