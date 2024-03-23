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
import java.util.Arrays;
import java.util.List;

@CrossOrigin // @CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TaskController {

    @GetMapping("/api/tasks")
    public List<Task> getAllTasks() {
        return Arrays.asList(
                new Task(1, "Task 1", "Description of Task 1 is here"),
                new Task(2, "Task 2", "Description of Task 2 is there"));
    }

    private static final String UPLOAD_DIR = "backend/src/main/resources";
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

    static class Task {
        private int id;
        private String title;
        private String description;

        // Constructor, getters, and setters
        public Task(int id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        // Setters - if you plan to modify tasks
        // Not necessary if the data is read-only
    }
}
