package test;

import main.manager.HistoryManager;
import main.manager.Managers;
import main.task.Status;
import main.task.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    public void savingMax10Tasks() {
        for (int i = 1; i < 12; i++) {

            historyManager.add(new Task("name" + i, "description" + i));
        }
        assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    public void deletingFirstAfter10new() {
        Task[] tasks = new Task[10];
        historyManager.add(new Task("name1", "description1"));
        for (int i = 2; i < 12; i++) {
            Task task = new Task("name" + i, "description" + i);
            historyManager.add(task);
            tasks[i - 2] = task;
        }


        assertArrayEquals(tasks, historyManager.getHistory().toArray());
    }

    @Test
    public void savingAllTasksBeforeAndAfterUpdating() {
        Task taskBeforeUpdate = new Task("name", " description");
        historyManager.add(taskBeforeUpdate);
        taskBeforeUpdate.setStatus(Status.DONE);
        historyManager.add(taskBeforeUpdate);
        Task[] tasks = {taskBeforeUpdate, taskBeforeUpdate};
        assertArrayEquals(tasks, historyManager.getHistory().toArray());

    }
}