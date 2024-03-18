package org.servlet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

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
