package org.graph;

public class CodeBlock {
    public String[] code;
    public String fileName;
    public int lineStart;

    public CodeBlock(String[] code, String fileName, int lineStart) {
        this.code = code;
        this.fileName = fileName;
        this.lineStart = lineStart;
    }

    @Override
    public String toString() {
        return "CodeBlock{" +
                "code=" + String.join("\n", code) +
                ", fileName='" + fileName + '\'' +
                ", lineStart=" + lineStart +
                '}';
    }
}
