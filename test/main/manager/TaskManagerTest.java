package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;
import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {
    protected final Task task1 = new Task("name", "description", START_TIME, THIRTY_MINUTES);
    protected final Task task2 = new Task("name2", " description2", START_TIME.plusHours(1), THIRTY_MINUTES);
    protected final Task task3 = new Task("task", "task", START_TIME.plusHours(2), THIRTY_MINUTES);

    protected final Epic epic1 = new Epic("name", "description");
    protected final Epic epic2 = new Epic("task", "task");
    protected final Epic epic3 = new Epic("task", "task");

    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Subtask subtask3;
    protected T taskManager;

    protected abstract T createTaskManager() throws IOException;


    @BeforeEach
    public void setUp() throws IOException {
        taskManager = createTaskManager();
    }

    public void add1SubtaskToEpic1() {
        taskManager.addTask(epic1);

        subtask1 = new Subtask("name", "description", epic1, START_TIME.plusDays(1), THIRTY_MINUTES);
        taskManager.addTask(subtask1);
    }

    public void add3SubtaskToEpic1() {
        taskManager.addTask(epic1);
        subtask1 = new Subtask("name", "description", epic1, START_TIME.plusHours(3), THIRTY_MINUTES);
        subtask2 = new Subtask("name", "description", epic1, START_TIME.plusHours(4), THIRTY_MINUTES);
        subtask3 = new Subtask("name", "description", epic1, START_TIME.plusHours(5), THIRTY_MINUTES);
        taskManager.addTask(subtask1);
        taskManager.addTask(subtask2);
        taskManager.addTask(subtask3);
    }

    public void oneSubtask() {
        subtask1 = new Subtask("name", "description", epic1, START_TIME.plusHours(3), THIRTY_MINUTES);

    }

    @Test
    public void canAddTask() {
        taskManager.addTask(task1);
        Task[] tasks = {task1};
        assertArrayEquals(tasks, taskManager.getAllTasks().toArray());
        assertEquals(task1, taskManager.findById(1));
    }

    @Test
    public void canAddEpic() {
        taskManager.addTask(epic1);
        Task[] tasks = {epic1};
        assertArrayEquals(tasks, taskManager.getAllEpic().toArray());
        assertEquals(epic1, taskManager.findById(1));
    }

    @Test
    public void canEddSubtaskWithExistingEpic() {
        add1SubtaskToEpic1();
        Task[] tasks = {subtask1};
        assertArrayEquals(tasks, taskManager.getAllSubtasks().toArray());
        assertEquals(subtask1, taskManager.findById(2));
        assertEquals(epic1, taskManager.findById(1));
    }

    @Test
    public void addIdIsDifferent() {
        taskManager.addTask(task1);
        task2.setId(1);
        assertEquals(task1, task2);
        taskManager.addTask(task2);
        assertNotEquals(task1, task2);
    }

    @Test
    public void taskAfterAddingIsNotChanged() {
        taskManager.addTask(task1);
        assertEquals("name", taskManager.findById(1).getName());
        assertEquals("description", taskManager.findById(1).getDescription());
        assertEquals(TaskType.TASK, taskManager.findById(1).getType());
    }


    @Test
    public void addingTaskToHistoryAfterFindingById() {
        taskManager.addTask(task1);
        taskManager.findById(1);
        Task[] tasks = {task1, task1};
        assertArrayEquals(tasks, taskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        Task[] tasks = {task1, task2, task3};
        assertArrayEquals(tasks, taskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForEpics() {
        taskManager.addTask(epic1);
        taskManager.addTask(epic2);
        taskManager.addTask(epic3);
        Task[] tasks = {epic1, epic2, epic3};
        assertArrayEquals(tasks, taskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForSubtasks() {
        add3SubtaskToEpic1();
        Task[] tasks = {epic1, subtask1, subtask2, subtask3};
        assertArrayEquals(tasks, taskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForSubtasksFromEpic() {
        add3SubtaskToEpic1();
        Task[] tasks = {epic1, subtask1, subtask2, subtask3};
        assertArrayEquals(tasks, taskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void removeAllSubtasksFromHistoryFromEpicWhenDeletingEpic() {
        add3SubtaskToEpic1();
        taskManager.deleteById(epic1.getId());
        Task[] tasks = {};
        assertArrayEquals(tasks, taskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void cantAddWhenTimeIntersect() {
        taskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("name", "description", epic1, START_TIME.plusHours(3), THIRTY_MINUTES);
        Subtask subtask2 = new Subtask("name", "description", epic1, START_TIME.plusHours(2).plusMinutes(45), THIRTY_MINUTES);
        taskManager.addTask(subtask1);
        assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(subtask2));
    }

    @Test
    public void doesTimeIntersectWith2SameTimeTasks() {
        taskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("name", "description", epic1, START_TIME.plusHours(3), THIRTY_MINUTES);
        Subtask subtask2 = new Subtask("name", "description", epic1, START_TIME.plusHours(2).plusMinutes(45), THIRTY_MINUTES);

        assertTrue(taskManager.doesTimeIntersect(subtask1, subtask2));
    }

    @Test
    public void doesTimeIntersectWith2DifferentTimeTasks() {
        taskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("name", "description", epic1, START_TIME.plusHours(3), THIRTY_MINUTES);
        Subtask subtask2 = new Subtask("name", "description", epic1, START_TIME.plusHours(4).plusMinutes(45), THIRTY_MINUTES);

        assertFalse(taskManager.doesTimeIntersect(subtask1, subtask2));
    }

}
