package org.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CFGBuilderTest {

    private CFGBuilder builder;
    private static final String TEST_FILE_PATH = "examples/MasterFileWithErrorCases.java";

    @BeforeEach
    void setUp() {
        builder = new CFGBuilder();
    }

    @Test
    void testBuildCFGs() {
        builder.buildCFGs(TEST_FILE_PATH);
        builder.getGlobalCFGMap();

        // Print out the keys of the globalCFGMap
        System.out.println("Keys of globalCFGMap:");
        builder.globalCFGMap.keySet().forEach(System.out::println);

        // Verify that a CFG was created for the method calculateSum
        assertTrue(builder.getGlobalCFGMap().containsKey("calculateSum(int num1, int num2)"),
                "CFG should contain 'calculateSum(int num1, int num2)'");
    }

    @Test
    void testReturnInBothBranchesNodeAndEdgeCount() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("returnInBothBranches(int a)");
        builder.printCFG("returnInBothBranches(int a)");
        // Expected number of nodes and edges
        int expectedNodeCount = 12; // Update this value based on your actual CFG
        int expectedEdgeCount = 13; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void testReturnInIFELSEBranch() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("returnInIfElseBranch(int a)");
        builder.printCFG("returnInIfElseBranch(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("aSimpleTest(java.lang.String[] var0)");
        builder.printCFG("aSimpleTest(java.lang.String[] var0)");
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
        CFG cfg = builder.getGlobalCFGMap().get("AssigmentWithoutDeclaration(int a)");
        builder.printCFG("AssigmentWithoutDeclaration(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("checkingIfElse(int a)");
        builder.printCFG("checkingIfElse(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("sampleDeclarationWithNoUse(int a)");
        builder.printCFG("sampleDeclarationWithNoUse(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("simplewhileReturn(int a)");
        builder.printCFG("simplewhileReturn(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("returnInInnerLoop(int a)");
        builder.printCFG("returnInInnerLoop(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("returnInOuterLoop(int a)");
        builder.printCFG("returnInOuterLoop(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("Ifstatementafterbreak(int a)");
        builder.printCFG("Ifstatementafterbreak(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("sampleForLoopProgram(int a)");
        builder.printCFG("sampleForLoopProgram(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("sampleIfProgram(int a)");
        builder.printCFG("sampleIfProgram(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("sampleForLoopInsideIF(int num1, int num2)");
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
        CFG cfg = builder.getGlobalCFGMap().get("sampleForLoop(int num1, int num2)");
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
        CFG cfg = builder.getGlobalCFGMap().get("main(java.lang.String[] args)");
        builder.printCFG("main(java.lang.String[] args)");
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
        CFG cfg = builder.getGlobalCFGMap().get("sampleWhileLoopWithBreakInsideNestedIFProgram(int a)");
        builder.printCFG("sampleWhileLoopWithBreakInsideNestedIFProgram(int a)");
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
        CFG cfg = builder.getGlobalCFGMap().get("isEvenNumber(int num)");
        builder.printCFG("isEvenNumber(int num)");
        // Expected number of nodes and edges
        int expectedNodeCount = 3; // Update this value based on your actual CFG
        int expectedEdgeCount = 2; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void otherStatementafterbreak() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("otherStatementafterbreak(int a)");
        builder.printCFG("otherStatementafterbreak(int a)");
        // Expected number of nodes and edges
        int expectedNodeCount = 11; // Update this value based on your actual CFG
        int expectedEdgeCount = 12; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void Loopstatementafterbreak() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("Loopstatementafterbreak(int a)");
        builder.printCFG("Loopstatementafterbreak(int a)");
        // Expected number of nodes and edges
        int expectedNodeCount = 11; // Update this value based on your actual CFG
        int expectedEdgeCount = 12; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void loopEndWithReturn() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("loopEndWithReturn(int a)");
        builder.printCFG("loopEndWithReturn(int a)");
        // Expected number of nodes and edges
        int expectedNodeCount = 11; // Update this value based on your actual CFG
        int expectedEdgeCount = 12; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }

    @Test
    void nestedLoopEndWithReturn() {
        builder.buildCFGs(TEST_FILE_PATH);
        CFG cfg = builder.getGlobalCFGMap().get("nestedLoopEndWithReturn(int a)");
        builder.printCFG("nestedLoopEndWithReturn(int a)");
        // Expected number of nodes and edges
        int expectedNodeCount = 15; // Update this value based on your actual CFG
        int expectedEdgeCount = 17; // Update this value based on your actual CFG

        assertEquals(expectedNodeCount, cfg.nodes.size(), "Number of nodes should match expected value");
        int actualEdgeCount = cfg.nodes.stream().mapToInt(node -> node.next.size()).sum();
        assertEquals(expectedEdgeCount, actualEdgeCount, "Number of edges should match expected value");
    }
}
