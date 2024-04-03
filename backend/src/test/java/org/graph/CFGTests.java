package org.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class CFGTest {

    private CFG cfg;
    private CodeBlock codeBlock;
    private Node entryNode;

    @BeforeEach
    void setUp() {
        cfg = new CFG();
        String[] codeLines = { "int x = 0;", "x++;", "return x;" };
        codeBlock = new CodeBlock(codeLines, "MyClass.java", 1);
        entryNode = cfg.createNode(codeBlock);
        cfg.setEntryNode(entryNode);
        cfg.setExitNode(entryNode); // Simplified for the example; normally the exit would be different.
    }

    @Test
    @DisplayName("Create node adds a new node to the CFG")
    void testCreateNode() {
        CodeBlock newCodeBlock = new CodeBlock(new String[] { "System.out.println(\"Hello World\");" }, "Main.java",
                10);
        Node newNode = cfg.createNode(newCodeBlock);
        assertNotNull(newNode, "createNode should return a non-null node");
        assertTrue(cfg.getNodes().contains(newNode), "New node should be added to the nodes list");
    }

    @Test
    @DisplayName("Set entry node should correctly set the entry node of the CFG")
    void testSetEntryNode() {
        assertEquals(entryNode, cfg.getEntryNode(), "The entry node should be the one that was set");
    }

    @Test
    @DisplayName("Set exit node should correctly set the exit node of the CFG")
    void testSetExitNode() {
        assertEquals(entryNode, cfg.getExitNode(), "The exit node should be the one that was set");
    }

    @Test
    @DisplayName("ToTextRepresentation should return the correct textual representation of the CFG")
    void testToTextRepresentation() {
        String expectedOutput = "CodeBlock{code=int x = 0;\nx++;\nreturn x;, fileName='MyClass.java', lineStart=1} (ID: "
                + entryNode.getId() + ", Comments: [])\n\n";
        String actualOutput = cfg.toTextRepresentation();
        System.out.println(actualOutput);
        assertEquals(expectedOutput, actualOutput,
                "The textual representation of CFG should match the expected format." + actualOutput);
    }
}
