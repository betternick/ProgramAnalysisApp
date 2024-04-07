package org.analysis;

import org.graph.CodeBlock;
import org.graph.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONObject;

class ExecTreeTests {
    private ExecTree rootExecTree;
    private Node mockNode;

    @BeforeEach
    void setUp() {
        // Assuming Node has an ID of 1 for testing purposes
        mockNode = new Node(new CodeBlock(new String[] { "Test" }, "TestFile.java", 1));
        mockNode.id = 1; // Directly setting the ID for simplicity in this mock setup

        // Initialize ExecTree with mocked Node
        rootExecTree = new ExecTree(mockNode, 100L, 500L, 0.5f);
    }

    @Test
    void testExecutionTimeCalculationForLeafNode() {
        rootExecTree.calculateExecutionTimes(200L);
        assertEquals(100, rootExecTree.getExecutionTimeInNano(),
                "Execution time should be correctly calculated for leaf node.");
    }

    @Test
    void testAddChildAndVerifyLeafNode() {
        ExecTree child = new ExecTree(null, 150L, 300L, 0.3f);
        rootExecTree.addChild(child);
        assertFalse(rootExecTree.isLeaf(), "Root node should not be a leaf after adding a child.");
    }

    @Test
    void testToJSON() {
        JSONObject json = rootExecTree.toJSON();
        assertNotNull(json, "JSON object should not be null.");
        assertEquals(1, json.getInt("id"), "JSON object should contain the correct node ID.");
        assertEquals(100L, json.getLong("startTimeInNano"), "JSON object should contain the correct start time.");
    }

    @Test
    void testExecutionTimeCalculationWithInvalidEndTime() {
        rootExecTree.calculateExecutionTimes(50L); // End time is before start time
        assertTrue(rootExecTree.getExecutionTimeInNano() < 0,
                "Execution time should be negative when end time is before start time.");
    }

    @Test
    void testToJSONWithoutCodeNode() {
        ExecTree rootWithoutNode = new ExecTree(null, 100L, 500L, 0.5f);
        JSONObject json = rootWithoutNode.toJSON();
        assertEquals(-1, json.getInt("id"), "JSON object should have an ID of -1 for root node without a code node.");
    }

}
