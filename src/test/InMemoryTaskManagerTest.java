package test;

import main.manager.InMemoryTaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    @Test
    public void canAddTask() {
        Task task1 = new Task("name", "description");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.addTask(task1);
        Task[] tasks = {task1};
        assertArrayEquals(tasks, inMemoryTaskManager.getAllTasks().toArray());
        assertEquals(task1, inMemoryTaskManager.findById(1));
    }

    @Test
    public void canAddEpic() {
        Epic epic1 = new Epic("name", "description");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.addTask(epic1);
        Task[] tasks = {epic1};
        assertArrayEquals(tasks, inMemoryTaskManager.getAllEpic().toArray());
        assertEquals(epic1, inMemoryTaskManager.findById(1));
    }

    @Test
    public void canEddSubtaskWithExistingEpic() {
        Epic epic = new Epic("name", "description");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic);
        inMemoryTaskManager.addTask(subtask);
        Task[] tasks = {subtask};
        assertArrayEquals(tasks, inMemoryTaskManager.getAllSubtasks().toArray());
        assertEquals(subtask, inMemoryTaskManager.findById(2));
        assertEquals(epic, inMemoryTaskManager.findById(1));
    }

    @Test
    public void addIdIsDifferent() {
        Task task1 = new Task("name1", " description1");
        Task task2 = new Task("name2", " description2");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.addTask(task1);
        task2.setId(1);
        assertEquals(task1, task2);
        inMemoryTaskManager.addTask(task2);
        assertNotEquals(task1, task2);
    }

    @Test
    public void taskAfterAddingIsNotChanged() {
        Task task1 = new Task("name", "description");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.addTask(task1);
        assertEquals("name", inMemoryTaskManager.findById(1).getName());
        assertEquals("description", inMemoryTaskManager.findById(1).getDescription());
        assertEquals(TaskType.TASK, inMemoryTaskManager.findById(1).getType());
    }

    @Test
    public void addingTaskToHistoryAfterFindingById() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = new Task("task", "task");
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.findById(1);
        Task[] tasks = {task};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForTasks() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("task", "task");
        Task task2 = new Task("task", "task");
        Task task3 = new Task("task", "task");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.getAllTasks();
        Task[] tasks = {task1, task2, task3};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForEpics() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic task1 = new Epic("task", "task");
        Epic task2 = new Epic("task", "task");
        Epic task3 = new Epic("task", "task");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.getAllEpic();
        Task[] tasks = {task1, task2, task3};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForSubtasks() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic("name", "description");
        inMemoryTaskManager.addTask(epic);
        Subtask task1 = new Subtask("task", "task", epic);
        Subtask task2 = new Subtask("task", "task", epic);
        Subtask task3 = new Subtask("task", "task", epic);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.getAllSubtasks();
        Task[] tasks = {task1, task2, task3};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForSubtasksFromEpic() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic("name", "description");
        inMemoryTaskManager.addTask(epic);
        Subtask task1 = new Subtask("task", "task", epic);
        Subtask task2 = new Subtask("task", "task", epic);
        Subtask task3 = new Subtask("task", "task", epic);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.getAllSubtaskFromEpic(1);
        Task[] tasks = {task1, task2, task3};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());

    }

}