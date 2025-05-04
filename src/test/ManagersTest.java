package test;

import main.manager.HistoryManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void CreateNewDefaultTaskManager() {

        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
        Task task = new Task("name", "description");
        taskManager.addTask(task);
        Task[] tasks = {task};
        assertArrayEquals(tasks, taskManager.getAllTasks().toArray());
    }

    @Test
    public void CreateNewDefaultHistoryManager() {

        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);

        Task task = new Task("name", "description");
        historyManager.add(task);
        Task[] tasks = {task};
        assertArrayEquals(tasks, historyManager.getHistory().toArray());
    }
}