package main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    public void setup() {
        taskService = new TaskService();
    }

    // Test that a task can be added with a unique ID
    @Test
    public void testAddTaskWithUniqueId() {
        Task task = new Task("1234567890", "TaskName", "Task description");
        taskService.addTask(task);

        Assertions.assertAll("taskAdded",
            () -> Assertions.assertEquals(task, taskService.getTask("1234567890")),
            () -> Assertions.assertNotNull(taskService.getTask("1234567890"))
        );
    }

    // Test that adding a task with a duplicate ID throws an exception
    @Test
    public void testAddTaskWithDuplicateId() {
        Task task1 = new Task("1234567890", "TaskName", "Task description");
        taskService.addTask(task1);

        Task task2 = new Task("1234567890", "AnotherTask", "Another description");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskService.addTask(task2);
        });
    }

    // Test that a task can be deleted by taskId
    @Test
    public void testDeleteTask() {
        Task task = new Task("1234567890", "TaskName", "Task description");
        taskService.addTask(task);
        taskService.deleteTask("1234567890");

        Assertions.assertAll("taskDeleted",
            () -> Assertions.assertNull(taskService.getTask("1234567890")),
            () -> Assertions.assertThrows(IllegalArgumentException.class, () -> 
                taskService.updateTask("1234567890", "NewName", "NewDescription"))
        );
    }

    // Test that task fields (name and description) can be updated by taskId
    @Test
    public void testUpdateTaskFields() {
        Task task = new Task("1234567890", "TaskName", "Task description");
        taskService.addTask(task);

        taskService.updateTask("1234567890", "UpdatedTaskName", "Updated task description");
        Task updatedTask = taskService.getTask("1234567890");

        Assertions.assertAll("taskUpdated",
            () -> Assertions.assertEquals("UpdatedTaskName", updatedTask.getName()),
            () -> Assertions.assertEquals("Updated task description", updatedTask.getDescription())
        );
    }
}
