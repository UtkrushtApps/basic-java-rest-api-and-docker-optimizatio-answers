package com.example.tasktracker;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getIncompleteTasks();
    }

    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody TaskRequest request) {
        Task t = taskService.addTask(request.getDescription());
        return ResponseEntity.ok(t);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable long id) {
        Task t = taskService.completeTask(id);
        if (t == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(t);
    }

    @GetMapping("/stats")
    public ResponseEntity<TaskStats> getTaskStats() {
        // Instant response; stats are updated asynchronously
        return ResponseEntity.ok(taskService.getCachedStats());
    }
}

class TaskRequest {
    private String description;

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}