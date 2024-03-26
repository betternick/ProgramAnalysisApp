package org.servlet;

import org.graph.CFGBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class AnalysisService {
    public static String UPLOAD_DIR = "backend/src/main/resources";

    public Boolean uploadFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath((Objects.requireNonNull(file.getOriginalFilename())));

        Path uploadPath = Paths.get(UPLOAD_DIR, fileName);
        try {
            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            CFGBuilder.main(null);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
