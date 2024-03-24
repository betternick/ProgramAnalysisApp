package org.servlet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@CrossOrigin // @CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AnalysisController {

    public static String UPLOAD_DIR = "backend/src/main/resources";

    @PostMapping("/upload")
    public ResponseEntity<Boolean> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath((file.getOriginalFilename()));

        Path uploadPath = Paths.get(UPLOAD_DIR, fileName);

        try {
            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok(true);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
