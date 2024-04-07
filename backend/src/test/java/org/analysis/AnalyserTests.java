package org.analysis;

import org.exception.CompilationException;
import org.graph.CFGBuilder;
import org.servlet.AnalysisService;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalyserTests {

    private static final String TEST_FILE_PATH = "examples/ShouldPass.java";

    private AnalysisService service = new AnalysisService();
    private static final String EXPECTED_JSON = """
              {
              "cpuUsage": 0,
              "executionTimeInNano": 10006291,
              "memoryUsage": 0,
              "children": [
                {
                  "cpuUsage": 0,
                  "executionTimeInNano": 3334208,
                  "memoryUsage": 14502832,
                  "id": 2,
                  "startTimeInNano": 281167215154958
                },
                {
                  "cpuUsage": 0.30695337,
                  "executionTimeInNano": 456250,
                  "memoryUsage": 14693024,
                  "id": 3,
                  "startTimeInNano": 281167218489166
                },
                {
                  "cpuUsage": 0.25307608,
                  "executionTimeInNano": 128917,
                  "memoryUsage": 14693024,
                  "id": 4,
                  "startTimeInNano": 281167218945416
                },
                {
                  "cpuUsage": 0.22979797,
                  "executionTimeInNano": 96000,
                  "memoryUsage": 14693024,
                  "id": 5,
                  "startTimeInNano": 281167219074333
                },
                {
                  "cpuUsage": 0.125,
                  "executionTimeInNano": 98833,
                  "memoryUsage": 14693024,
                  "id": 10,
                  "startTimeInNano": 281167219170333
                }
              ],
              "id": -1,
              "startTimeInNano": 281167209262875
            }
            """;; // Expected JSON representation of the exec tree

    public static Path createTempFileWithContent(String prefix, String suffix, String content) throws IOException {
        Path tempFile = Files.createTempFile(prefix, suffix);
        Files.writeString(tempFile, content);
        return tempFile;
    }

    @Test
    void testWithInvalidLogFileFormat() throws Exception {
        String logContent = "This is not a valid log format";

        Path tempLogFile = createTempFileWithContent("invalid_log_format", ".txt", logContent);

        Analyser analyser = new Analyser("cfgMap.ser");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            analyser.analyze(tempLogFile.toString());
        });

        String errorMessage = exception.getMessage();

        assertTrue(errorMessage.contains("The first line of the log file should be Enter:main(java.lang.String[])"),
                "Expected to catch an exception indicating an invalid log file format.");

        Files.deleteIfExists(tempLogFile); // Cleanup
    }

    @Test
    void testAnalyzeEvaluatesCorrectNumberOfNodes() throws CompilationException {
        CFGBuilder cfg = new CFGBuilder();
        cfg.buildCFGs(TEST_FILE_PATH);
        cfg.serializeMap("cfgMap.ser");

        String graphPath = "cfgMap.ser";
        cfg.serializeMap(graphPath);
        var IDs = cfg.getAllNodeIds();
        Analyser analyser = new Analyser(graphPath);
        analyser.analyze("src/test/resources/sampleTarget.log");
        Map<Integer, ExecTreeStats> nodeStats = new HashMap<>();
        for (Integer id : IDs) {
            ExecTreeStats stats = analyser.getStat(id);
            nodeStats.put(id, stats);
        }
        assertFalse(nodeStats.isEmpty());
        assertTrue(nodeStats.size() == 43);
    }

    // the target log is given by hand, not generated dynamically, so some ID in the log can't be found in the CFG
    // @Test
    // void testAnalyzeReturnsCorrectExecTree() {
    //     String expectedExecTree = EXPECTED_JSON.replace("\n ", "").replace("  ", "").replace(" ", "").replace("\n", "");
    //     CFGBuilder cfg = new CFGBuilder();
    //     cfg.buildCFGs(TEST_FILE_PATH);
    //     cfg.serializeMap("cfgMap.ser");

    //     String graphPath = "cfgMap.ser";
    //     cfg.serializeMap(graphPath);
    //     var IDs = cfg.getAllNodeIds();
    //     Analyser analyser = new Analyser(graphPath);
    //     try {
    //         analyser.analyze("src/test/resources/simpleTarget.log");
    //     } catch (IllegalArgumentException e) {
    //         fail("Failed to analyze log file" + "\nCFG Map: " + cfg.getGlobalCFGMap().toString());
    //     }
    //     String actualExecTree = analyser.getExecTreeAsJson();
    //     assertTrue(actualExecTree.equals(expectedExecTree));
    // }
}
