package com.example.tasktracker;

public class TaskStats {
    private final int incompleteTasks;
    private final long completedTasks;

    public TaskStats(int incompleteTasks, long completedTasks) {
        this.incompleteTasks = incompleteTasks;
        this.completedTasks = completedTasks;
    }

    public int getIncompleteTasks() {
        return incompleteTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }
}
