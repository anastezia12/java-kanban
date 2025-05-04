package test;

import main.manager.InMemoryHistoryManager;
import main.task.Status;
import main.task.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    @Test
    public void savingMax10Tasks() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        inMemoryHistoryManager.add(new Task("name1", "description1"));
        inMemoryHistoryManager.add(new Task("name2", "description2"));
        inMemoryHistoryManager.add(new Task("name3", "description3"));
        inMemoryHistoryManager.add(new Task("name4", "description4"));
        inMemoryHistoryManager.add(new Task("name5", "description5"));
        inMemoryHistoryManager.add(new Task("name6", "description6"));
        inMemoryHistoryManager.add(new Task("name7", "description7"));
        inMemoryHistoryManager.add(new Task("name8", "description8"));
        inMemoryHistoryManager.add(new Task("name9", "description9"));
        inMemoryHistoryManager.add(new Task("name10", "description10"));
        inMemoryHistoryManager.add(new Task("name11", "description11"));
        assertEquals(10, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void deletingFirstAfter10new() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task task1 = new Task("name1", "description1");
        Task task2 = new Task("name2", "description2");
        Task task3 = new Task("name3", "description3");
        Task task4 = new Task("name4", "description4");
        Task task5 = new Task("name5", "description5");
        Task task6 = new Task("name6", "description6");
        Task task7 = new Task("name7", "description7");
        Task task8 = new Task("name8", "description8");
        Task task9 = new Task("name9", "description9");
        Task task10 = new Task("name10", "description10");
        Task task11 = new Task("name11", "description11");

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.add(task6);
        inMemoryHistoryManager.add(task7);
        inMemoryHistoryManager.add(task8);
        inMemoryHistoryManager.add(task9);
        inMemoryHistoryManager.add(task10);
        inMemoryHistoryManager.add(task11);

        Task[] tasks = {task2, task3, task4, task5, task6, task7, task8, task9, task10, task11};
        assertArrayEquals(tasks, inMemoryHistoryManager.getHistory().toArray());
    }

    @Test
    public void savingAllTasksBeforeAndAfterUpdating() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task taskBeforeUpdate = new Task("name", " description");
        inMemoryHistoryManager.add(taskBeforeUpdate);
        taskBeforeUpdate.setStatus(Status.DONE);
        inMemoryHistoryManager.add(taskBeforeUpdate);
        Task[] tasks = {taskBeforeUpdate, taskBeforeUpdate};
        assertArrayEquals(tasks, inMemoryHistoryManager.getHistory().toArray());

    }
}