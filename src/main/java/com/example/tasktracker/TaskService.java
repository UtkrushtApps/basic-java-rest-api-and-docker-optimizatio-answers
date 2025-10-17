package com.example.tasktracker;

import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final ConcurrentHashMap<Long, Task> incompleteTasks = new ConcurrentHashMap<>();
    // We only hold completed count, not all completed tasks (fixes memory leak)
    private final AtomicLong completedTaskCount = new AtomicLong(0);
    private final AtomicLong idCounter = new AtomicLong(0);

    // For async stats calculation
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile TaskStats cachedStats = new TaskStats(0, 0);
    private final Object statsLock = new Object();
    private volatile long lastStatsUpdateAt = 0;
    private static final long STATS_UPDATE_INTERVAL_MILLIS = 2000; // at most every 2 seconds

    public Task addTask(String description) {
        long id = idCounter.incrementAndGet();
        Task t = new Task(id, description, false);
        incompleteTasks.put(id, t);
        triggerStatsUpdate();
        return t;
    }

    public Task completeTask(long id) {
        Task removed = incompleteTasks.remove(id);
        if (removed != null && !removed.isCompleted()) {
            completedTaskCount.incrementAndGet();
            triggerStatsUpdate();
            return new Task(id, removed.getDescription(), true);
        }
        return null;
    }

    public List<Task> getIncompleteTasks() {
        return new ArrayList<>(incompleteTasks.values());
    }

    public TaskStats getCachedStats() {
        // Fast response, do not calculate here
        return cachedStats;
    }

    private void triggerStatsUpdate() {
        // Only allow one concurrent update; debounce with interval
        synchronized (statsLock) {
            long now = System.currentTimeMillis();
            if (now - lastStatsUpdateAt > STATS_UPDATE_INTERVAL_MILLIS) {
                lastStatsUpdateAt = now;
                executor.submit(this::updateStats);
            }
        }
    }

    private void updateStats() {
        // Simulate heavy stats computation
        int incomplete;
        long completed;
        incomplete = incompleteTasks.size();
        completed = completedTaskCount.get();
        // Sleep a bit to mock expensive computation
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
        cachedStats = new TaskStats(incomplete, completed);
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
}
