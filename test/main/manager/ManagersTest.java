package main.manager;

import main.task.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
    private LocalDateTime startTime = LocalDateTime.parse("11:12 12.10.24", dateTimeFormatter);

    @Test
    public void createNewDefaultTaskManager() {

        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
        Task task = new Task("name", "description", startTime, Duration.ofMinutes(30));
        taskManager.addTask(task);
        Task[] tasks = {task};
        assertArrayEquals(tasks, taskManager.getAllTasks().toArray());
    }

    @Test
    public void createNewDefaultHistoryManager() {

        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);

        Task task = new Task("name", "description", startTime, Duration.ofMinutes(30));
        historyManager.add(task);
        Task[] tasks = {task};
        assertArrayEquals(tasks, historyManager.getHistory().toArray());
    }
}