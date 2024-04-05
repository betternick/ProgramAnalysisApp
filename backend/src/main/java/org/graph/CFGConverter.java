package org.graph;

/// CHAT GPT use acknowledgement: It was used at all different stages to write and debug code in the graph package.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CFGConverter {

    public static Map<String, Object> convertCFGToEdgesAndNodes(CFG cfg) {
        Map<String, Object> jsonCFG = new HashMap<>();
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> edges = new ArrayList<>();

        for (Node node : cfg.nodes) {
            Map<String, Object> jsonNode = new HashMap<>();
            jsonNode.put("id", String.valueOf(node.id));
            jsonNode.put("codeBlock", node.codeBlock);
            // This adds comments
            jsonNode.put("comments", node.comments);
            nodes.add(jsonNode);

            for (Node successor : node.next) {
                Map<String, Object> jsonEdge = new HashMap<>();
                jsonEdge.put("id", "e" + node.id + "-" + successor.id);
                jsonEdge.put("source", String.valueOf(node.id));
                jsonEdge.put("target", String.valueOf(successor.id));
                edges.add(jsonEdge);
            }
        }

        jsonCFG.put("nodes", nodes);
        jsonCFG.put("edges", edges);

        return jsonCFG;
    }

    public static String convertAllCFGs(Map<String, CFG> globalCFGMap) {
        Map<String, Map<String, Object>> convertedCFGs = new HashMap<>();

        for (Map.Entry<String, CFG> entry : globalCFGMap.entrySet()) {
            String methodName = entry.getKey();
            CFG cfg = entry.getValue();
            Map<String, Object> convertedCFG = convertCFGToEdgesAndNodes(cfg);
            convertedCFGs.put(methodName, convertedCFG);
        }

        // Convert the convertedCFGs map to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(convertedCFGs);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
