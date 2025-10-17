package com.example.tasktracker;

public class Task {
    private final long id;
    private final String description;
    private final boolean completed;

    public Task(long id, String description, boolean completed) {
        this.id = id;
        this.description = description;
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
}