package main;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class TaskService {
    private Map<String, Task> tasks = new HashMap<>();

    // Add a new task
    public void addTask(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            throw new IllegalArgumentException("Task ID must be unique");
        }
        tasks.put(task.getTaskId(), task);
    }

    // Delete a task by taskId
    public void deleteTask(String taskId) {
        if (!tasks.containsKey(taskId)) {
            throw new IllegalArgumentException("Task ID not found");
        }
        tasks.remove(taskId);
    }

    // Update task fields by taskId (name and description)
    public void updateTask(String taskId, String name, String description) {
        if (!tasks.containsKey(taskId)) {
            throw new IllegalArgumentException("Task ID not found");
        }
        Task task = tasks.get(taskId);
        task.setName(name);
        task.setDescription(description);
    }

    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }

    // -------------------- Enhancement: Search & Filtering --------------------
    public List<Task> searchTasks(String criteria) {
        List<Task> results = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getName().toLowerCase().contains(criteria.toLowerCase()) ||
                task.getDescription().toLowerCase().contains(criteria.toLowerCase())) {
                results.add(task);
            }
        }
        return results;
    }
}
