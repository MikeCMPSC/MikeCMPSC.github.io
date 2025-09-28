package main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskTest {

    // Test valid task ID
    @Test
    public void testValidTaskId() {
        Task task = new Task("1234567890", "TaskName", "Task description");
        Assertions.assertAll("validTaskId",
            () -> Assertions.assertNotNull(task, "Task should not be null"),
            () -> Assertions.assertEquals("1234567890", task.getTaskId(), "Task ID should be '1234567890'")
        );
    }

    // Test null task ID
    @Test
    public void testNullTaskId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Task(null, "TaskName", "Task description");
        }, "Null task ID should throw an exception");
    }

    // Test task ID longer than 10 characters
    @Test
    public void testTaskIdLongerThan10Characters() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Task("12345678901", "TaskName", "Task description");
        }, "Task ID longer than 10 characters should throw an exception");
    }

    // Test valid task name
    @Test
    public void testValidTaskName() {
        Task task = new Task("1234567890", "TaskName", "Task description");
        Assertions.assertAll("validTaskName",
            () -> Assertions.assertNotNull(task.getName(), "Task name should not be null"),
            () -> Assertions.assertEquals("TaskName", task.getName(), "Task name should be 'TaskName'")
        );
    }

    // Test null task name
    @Test
    public void testNullTaskName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Task("1234567890", null, "Task description");
        }, "Null task name should throw an exception");
    }

    // Test task name longer than 20 characters
    @Test
    public void testTaskNameLongerThan20Characters() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Task("1234567890", "ThisNameIsWayTooLongForValidation", "Task description");
        }, "Task name longer than 20 characters should throw an exception");
    }

    // Test valid task description
    @Test
    public void testValidTaskDescription() {
        Task task = new Task("1234567890", "TaskName", "Valid description here.");
        Assertions.assertAll("validTaskDescription",
            () -> Assertions.assertNotNull(task.getDescription(), "Task description should not be null"),
            () -> Assertions.assertEquals("Valid description here.", task.getDescription(), "Task description should be 'Valid description here.'")
        );
    }

    // Test null task description
    @Test
    public void testNullTaskDescription() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Task("1234567890", "TaskName", null);
        }, "Null task description should throw an exception");
    }

    // Test task description longer than 50 characters
    @Test
    public void testTaskDescriptionLongerThan50Characters() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Task("1234567890", "TaskName", "This description is way too long to be considered valid.");
        }, "Task description longer than 50 characters should throw an exception");
    }
}
