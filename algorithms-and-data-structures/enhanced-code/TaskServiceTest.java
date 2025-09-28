package test;

import main.Task;
import main.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskService.addTask(new Task("1", "Write Report", "Complete project report by Monday"));
        taskService.addTask(new Task("2", "Code Review", "Review the pull requests for bug fixes"));
        taskService.addTask(new Task("3", "Meeting", "Team sync-up meeting on Wednesday"));
    }

    @Test
    void testSearchTasksByName() {
        List<Task> results = taskService.searchTasks("Code");
        assertEquals(1, results.size());
        assertEquals("2", results.get(0).getTaskId());
    }

    @Test
    void testSearchTasksByDescription() {
        List<Task> results = taskService.searchTasks("report");
        assertEquals(1, results.size());
        assertEquals("1", results.get(0).getTaskId());
    }

    @Test
    void testSearchTasksCaseInsensitive() {
        List<Task> results = taskService.searchTasks("MEETING");
        assertEquals(1, results.size());
        assertEquals("3", results.get(0).getTaskId());
    }

    @Test
    void testSearchTasksNoResults() {
        List<Task> results = taskService.searchTasks("nonexistent");
        assertTrue(results.isEmpty());
    }
}
