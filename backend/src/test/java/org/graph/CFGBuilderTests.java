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
        assertTrue(builder.getGlobalCFGMap().containsKey("calculateSum(int,int)"), "CFG should contain 'calculateSum()'");
    }
}