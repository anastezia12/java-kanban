package main.manager;

import main.task.Task;
import org.junit.jupiter.api.Test;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void createNewDefaultTaskManager() {

        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
        Task task = new Task("name", "description", START_TIME, THIRTY_MINUTES);
        taskManager.addTask(task);
        Task[] tasks = {task};
        assertArrayEquals(tasks, taskManager.getAllTasks().toArray());
    }

    @Test
    public void createNewDefaultHistoryManager() {

        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);

        Task task = new Task("name", "description", START_TIME, THIRTY_MINUTES);
        historyManager.add(task);
        Task[] tasks = {task};
        assertArrayEquals(tasks, historyManager.getHistory().toArray());
    }
}