package org.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;

class CFGConverterTest {

    private CFG cfg;
    private Node node1, node2;

    @BeforeEach
    void setUp() {
        cfg = new CFG();
        CodeBlock cb1 = new CodeBlock(new String[] {"code1"}, "File1.java", 1);
        CodeBlock cb2 = new CodeBlock(new String[] {"code2"}, "File1.java", 2);

        node1 = cfg.createNode(cb1);
        node2 = cfg.createNode(cb2);
        node1.addNext(node2); // Establish a connection for the test
    }

    @Test
    void testConvertCFGToEdgesAndNodes() {
        Map<String, Object> jsonCFG = CFGConverter.convertCFGToEdgesAndNodes(cfg);
        assertNotNull(jsonCFG, "The converted CFG should not be null");
        assertTrue(jsonCFG.containsKey("nodes"), "The map should contain 'nodes'");
        assertTrue(jsonCFG.containsKey("edges"), "The map should contain 'edges'");
    }

    @Test
    void testConvertAllCFGs() {
        Map<String, CFG> globalCFGMap = new HashMap<>();
        globalCFGMap.put("method1", cfg);
        String json = CFGConverter.convertAllCFGs(globalCFGMap);
        assertNotNull(json, "The JSON string should not be null");
    }
}

