package org.servlet;

import org.analysis.ExecTreeStats;
import org.exception.CompilationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@CrossOrigin // @CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AnalysisController {

    @Autowired
    AnalysisService service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String result = service.uploadFile(file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    @PostMapping("/execute")
    public ResponseEntity<Map<Integer, ExecTreeStats>> executeFile() {
        try {
            Map<Integer, ExecTreeStats> result = service.executeFile();
            return ResponseEntity.ok(result);
        } catch (CompilationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
        }
    }
}
