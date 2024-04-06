package org.servlet;

import org.analysis.Analyser;
import org.analysis.ExecTreeStats;
import org.exception.CompilationException;
import org.graph.CFGBuilder;
import org.graph.CFGConverter;
import org.profile.Executor;
import org.profile.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

@Service
public class AnalysisService {
    public static String UPLOAD_DIR = "src/main/resources";

    public String filePath = "";

    private CFGBuilder builder = new CFGBuilder();

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath((Objects.requireNonNull(file.getOriginalFilename())));

        Path uploadPath = Paths.get(UPLOAD_DIR, fileName);
        Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        filePath = uploadPath.toString();
        builder.buildCFGs(filePath);
        return CFGConverter.convertAllCFGs(CFGBuilder.globalCFGMap);
    }

    public Map<Integer, ExecTreeStats> executeFile() throws CompilationException {
        String graphPath = "cfgMap.ser";
        String className = getClassName(filePath);
        String logPath = Log.getLogPath();

        builder.serializeMap(graphPath);
        var IDs = builder.getAllNodeIds();

        Executor executor = Executor.getInstance();
        System.out.println(filePath);
        System.out.println(className);
        System.out.println(graphPath);

        executor.execute(filePath, className, graphPath, logPath);
        Analyser analyser = new Analyser(graphPath);
        analyser.analyze(logPath);

        Map<Integer, ExecTreeStats> nodeStats = new HashMap<>();
        for (Integer id : IDs) {
            ExecTreeStats stats = analyser.getStat(id);
            nodeStats.put(id, stats);
        }

        return nodeStats;
    }

    // This method written by ChatGPT 3.5 with slight edits
    public String getClassName(String filePath) {
        String packageName = null;
        String className = null;
        String customPackageName = "src.main.resources";
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            StringBuilder modifiedContent = new StringBuilder();
            modifiedContent.append("package ").append(customPackageName).append(";\n");
            packageName = customPackageName;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("public class")) {
                    className = line.substring(line.indexOf("class") + 6, line.lastIndexOf("{")).trim();
                    modifiedContent.append(line).append("\n");
                } else if (!line.startsWith("package")) {
                    modifiedContent.append(line).append("\n");
                }
            }
            scanner.close();
            // Rewrite the file with modified content if necessary
            FileWriter writer = new FileWriter(file);
            writer.write(modifiedContent.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (packageName != null && className != null) {
            return packageName + "." + className;
        } else if (className != null) {
            return className;
        } else {
            return null;
        }
    }
}
