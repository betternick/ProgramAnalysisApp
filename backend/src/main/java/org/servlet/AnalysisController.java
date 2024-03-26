package org.servlet;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    AnalysisService service;

    @PostMapping("/upload")
    public ResponseEntity<Boolean> uploadFile(@RequestParam("file") MultipartFile file) {
        Boolean result = service.uploadFile(file);
        if (result)
            return ResponseEntity.ok(true);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
    }
}
