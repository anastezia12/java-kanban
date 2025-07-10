package main.manager;

import main.manager.InMemory.InMemoryHistoryManager;
import main.task.Status;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import static main.manager.TImeConstants.START_TIME;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest extends TaskManagerTest<TaskManager> {


    @Override
    protected TaskManager createTaskManager() throws IOException {
        return Managers.getDefault();
    }

    @Override
    protected HistoryManager createHistoryManager() {
        return new InMemoryHistoryManager();
    }

    @Test
    public void savingTasksInOrder() {
        List<Task> tasks = new LinkedList<>();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task2);
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task2);
        assertArrayEquals(tasks.toArray(), historyManager.getHistory().toArray());
    }

    @Test
    public void allTasksInLastOrderAfterAction() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        assertArrayEquals(new Task[]{task1, task2, task1}, taskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void deletingTaskFromHistory() {
        Task task1 = new Task("name1", "desc1", START_TIME, Duration.ofMinutes(30));
        task1.setId(1);
        historyManager.add(task1);
        Task task2 = new Task("name2", "desc2", START_TIME.plusHours(30), Duration.ofMinutes(30));
        task2.setId(2);
        historyManager.add(task2);
        assertEquals(2, historyManager.getHistory().size());
        historyManager.remove(1);
        assertArrayEquals(new Task[]{task2}, historyManager.getHistory().toArray());
    }


}