package org.graph;

import java.io.Serializable;

public class CodeBlock implements Serializable {
    public String[] code;
    public String fileName;
    public int lineStart;

    public CodeBlock(String[] code, String fileName, int lineStart) {
        this.code = code;
        this.fileName = fileName;
        this.lineStart = lineStart;
    }

    public int getLineStart() {
        return lineStart;
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
