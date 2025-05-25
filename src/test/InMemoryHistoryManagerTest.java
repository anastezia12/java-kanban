package test;

import main.manager.HistoryManager;
import main.manager.InMemoryHistoryManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Status;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault();

    @Test
    public void savingTasksInOrder() {
        List<Task> tasks = new LinkedList<>();
        for (int i = 1; i < 12; i++) {
            Task task = new Task(i,"name" + i, "description" + i, Status.IN_PROGRESS);
            historyManager.add(task);
            tasks.add(task);
        }
        assertArrayEquals(tasks.toArray(), historyManager.getHistory().toArray());
    }

    @Test
    public void allTasksInLastOrderAfterAction() {
        Task task1 = new Task("name1", " description1");
        taskManager.addTask(task1);
        Task task2 = new Task("name2", " description2");
        taskManager.addTask(task2);
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        assertArrayEquals(new Task[]{task2, task1}, taskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void deletingTaskFromHistory(){
        Task task1 = new Task("name1", "desc1");
        task1.setId(1);
        historyManager.add(task1);
        Task task2 = new Task("name2", "desc2");
        task2.setId(2);
        historyManager.add(task2);
        assertEquals(2, historyManager.getHistory().size());
        historyManager.remove(1);
        assertArrayEquals(new Task[]{task2}, historyManager.getHistory().toArray());
    }


}