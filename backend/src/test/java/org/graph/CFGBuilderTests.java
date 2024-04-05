package org.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CFGBuilderTest {

    private CFGBuilder builder;
    private static final String TEST_FILE_PATH = "examples/Simple.java";

    @BeforeEach
    void setUp() {
        builder = new CFGBuilder();
    }

    @Test
    void testBuildCFGs() {
        builder.buildCFGs(TEST_FILE_PATH);
        builder.getGlobalCFGMap();

        // Verify that a CFG was created for the method calculateSum
        assertTrue(builder.getGlobalCFGMap().containsKey("calculateSum(int,int)"),
                "CFG should contain 'calculateSum()'");
    }

    @Test
    void testReturnInBothBranchesNodeAndEdgeCount() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("returnInBothBranches(int)");
        builder.printCFG("returnInBothBranches(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 14; // Update this value based on your actual CFG
        int expectedEdgeCount = 16; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void testReturnInIFELSEBranch() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("returnInIfElseBranch(int)");
        builder.printCFG("returnInIfElseBranch(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 12; // Update this value based on your actual CFG
        int expectedEdgeCount = 13; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void aSimpletest() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("aSimpleTest(java.lang.String[])");
        builder.printCFG("aSimpleTest(java.lang.String[])");
        // Expected number of nodes and edges
        int expectedNodeCount = 5; // Update this value based on your actual CFG
        int expectedEdgeCount = 4; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void AssigmentWithoutDeclaration() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("AssigmentWithoutDeclaration(int)");
        builder.printCFG("AssigmentWithoutDeclaration(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 14; // Update this value based on your actual CFG
        int expectedEdgeCount = 14; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void checkingIfElse() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("checkingIfElse(int)");
        builder.printCFG("checkingIfElse(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 17; // Update this value based on your actual CFG
        int expectedEdgeCount = 18; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void sampleDeclarationWithNoUse() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("sampleDeclarationWithNoUse(int)");
        builder.printCFG("sampleDeclarationWithNoUse(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 12; // Update this value based on your actual CFG
        int expectedEdgeCount = 13; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void simplewhileReturn() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("simplewhileReturn(int)");
        builder.printCFG("simplewhileReturn(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 12; // Update this value based on your actual CFG
        int expectedEdgeCount = 13; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void returnInInnerLoop() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("returnInInnerLoop(int)");
        builder.printCFG("returnInInnerLoop(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 11; // Update this value based on your actual CFG
        int expectedEdgeCount = 12; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void returnInOuterLoop() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("returnInOuterLoop(int)");
        builder.printCFG("returnInOuterLoop(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 18; // Update this value based on your actual CFG
        int expectedEdgeCount = 20; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void Ifstatementafterbreak() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("Ifstatementafterbreak(int)");
        builder.printCFG("Ifstatementafterbreak(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 11; // Update this value based on your actual CFG
        int expectedEdgeCount = 12; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void sampleForLoopProgram() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("sampleForLoopProgram(int)");
        builder.printCFG("sampleForLoopProgram(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 11; // Update this value based on your actual CFG
        int expectedEdgeCount = 12; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void sampleIfProgram() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("sampleIfProgram(int)");
        builder.printCFG("sampleIfProgram(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 13; // Update this value based on your actual CFG
        int expectedEdgeCount = 14; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void sampleForLoopInsideIF() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("sampleForLoopInsideIF(int,int)");
        builder.printCFG("sampleForLoopInsideIF(int,int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 25; // Update this value based on your actual CFG
        int expectedEdgeCount = 27; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void sampleForLoop() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("sampleForLoop(int,int)");
        builder.printCFG("sampleForLoop(int,int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 18; // Update this value based on your actual CFG
        int expectedEdgeCount = 19; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void main() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("main(java.lang.String[])");
        builder.printCFG("main(java.lang.String[])");
        // Expected number of nodes and edges
        int expectedNodeCount = 26; // Update this value based on your actual CFG
        int expectedEdgeCount = 27; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void sampleWhileLoopWithBreakInsideNestedIFProgram() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("sampleWhileLoopWithBreakInsideNestedIFProgram(int)");
        builder.printCFG("sampleWhileLoopWithBreakInsideNestedIFProgram(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 19; // Update this value based on your actual CFG
        int expectedEdgeCount = 22; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void singleLineFunction() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("singleLineFunction()");
        builder.printCFG("singleLineFunction()");
        // Expected number of nodes and edges
        int expectedNodeCount = 3; // Update this value based on your actual CFG
        int expectedEdgeCount = 2; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void isEvenNumber() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("isEvenNumber(int)");
        builder.printCFG("isEvenNumber(int)");
        // Expected number of nodes and edges
        int expectedNodeCount = 3; // Update this value based on your actual CFG
        int expectedEdgeCount = 2; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }
}
