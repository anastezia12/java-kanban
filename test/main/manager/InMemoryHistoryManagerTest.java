package main.manager;

import main.manager.InMemory.InMemoryHistoryManager;
import main.task.Status;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    protected final Task task1 = new Task("name", "description", START_TIME, THIRTY_MINUTES);
    protected final Task task2 = new Task("name2", " description2", START_TIME.plusHours(1), THIRTY_MINUTES);
    protected final Task task3 = new Task("task", "task", START_TIME.plusHours(2), THIRTY_MINUTES);

    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    public void savingNewTasksInOrder() {
        List<Task> tasks = new LinkedList<>();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        assertArrayEquals(tasks.toArray(), historyManager.getHistory().toArray());
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
        Task[] expected = new Task[3];
        historyManager.add(task1);
        historyManager.add(task2);
        expected[0] = task1.copy();
        expected[1] = task2.copy();
        task1.setStatus(Status.DONE);
        historyManager.add(task1);
        expected[2] = task1.copy();
        assertArrayEquals(expected, historyManager.getHistory().toArray());
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