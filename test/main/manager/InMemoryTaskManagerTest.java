package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
    private final TaskManager inMemoryTaskManager = Managers.getDefault();
    private final Epic epic1 = new Epic("name", "description");
    private final Epic epic2 = new Epic("task", "task");
    private final Epic epic3 = new Epic("task", "task");
    private LocalDateTime startTime = LocalDateTime.parse("11:12 12.10.24", dateTimeFormatter);
    private final Task task1 = new Task("name", "description", startTime, Duration.ofMinutes(30));
    private final Task task2 = new Task("name2", " description2", startTime.plusHours(1), Duration.ofMinutes(30));
    private final Task task3 = new Task("task", "task", startTime.plusHours(2), Duration.ofMinutes(30));

    @Test
    public void canAddTask() {
        inMemoryTaskManager.addTask(task1);
        Task[] tasks = {task1};
        assertArrayEquals(tasks, inMemoryTaskManager.getAllTasks().toArray());
        assertEquals(task1, inMemoryTaskManager.findById(1));
    }

    @Test
    public void canAddEpic() {
        inMemoryTaskManager.addTask(epic1);
        Task[] tasks = {epic1};
        assertArrayEquals(tasks, inMemoryTaskManager.getAllEpic().toArray());
        assertEquals(epic1, inMemoryTaskManager.findById(1));
    }

    @Test
    public void canEddSubtaskWithExistingEpic() {
        inMemoryTaskManager.addTask(epic1);

        Subtask subtask = new Subtask("name", "description", epic1, startTime.plusDays(1), Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(subtask);
        Task[] tasks = {subtask};
        assertArrayEquals(tasks, inMemoryTaskManager.getAllSubtasks().toArray());
        assertEquals(subtask, inMemoryTaskManager.findById(2));
        assertEquals(epic1, inMemoryTaskManager.findById(1));
    }

    @Test
    public void addIdIsDifferent() {
        inMemoryTaskManager.addTask(task1);
        task2.setId(1);
        assertEquals(task1, task2);
        inMemoryTaskManager.addTask(task2);
        assertNotEquals(task1, task2);
    }

    @Test
    public void taskAfterAddingIsNotChanged() {
        inMemoryTaskManager.addTask(task1);
        assertEquals("name", inMemoryTaskManager.findById(1).getName());
        assertEquals("description", inMemoryTaskManager.findById(1).getDescription());
        assertEquals(TaskType.TASK, inMemoryTaskManager.findById(1).getType());
    }

    @Test
    public void addingTaskToHistoryAfterFindingById() {
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.findById(1);
        Task[] tasks = {task1};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForTasks() {
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.getAllTasks();
        Task[] tasks = {task1, task2, task3};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForEpics() {
        inMemoryTaskManager.addTask(epic1);
        inMemoryTaskManager.addTask(epic2);
        inMemoryTaskManager.addTask(epic3);
        inMemoryTaskManager.getAllEpic();
        Task[] tasks = {epic1, epic2, epic3};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForSubtasks() {
        inMemoryTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("name", "description", epic1, startTime.plusHours(3), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("name", "description", epic1, startTime.plusHours(4), Duration.ofMinutes(30));
        Subtask subtask3 = new Subtask("name", "description", epic1, startTime.plusHours(5), Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(subtask1);
        inMemoryTaskManager.addTask(subtask2);
        inMemoryTaskManager.addTask(subtask3);
        inMemoryTaskManager.getAllSubtasks();
        Task[] tasks = {epic1, subtask1, subtask2, subtask3};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void addingTaskToHistoryAfterLookingForSubtasksFromEpic() {
        inMemoryTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("task", "task", epic1, startTime.plusHours(3), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("task", "task", epic1, startTime.plusHours(4), Duration.ofMinutes(30));
        Subtask subtask3 = new Subtask("task", "task", epic1, startTime.plusHours(5), Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(subtask1);
        inMemoryTaskManager.addTask(subtask2);
        inMemoryTaskManager.addTask(subtask3);
        inMemoryTaskManager.getAllSubtaskFromEpic(1);
        Task[] tasks = {epic1, subtask1, subtask2, subtask3};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void removeAllSubtasksFromHistoryFromEpicWhenDeletingEpic() {
        inMemoryTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("task", "task", epic1, startTime.plusHours(3), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("task", "task", epic1, startTime.plusHours(4), Duration.ofMinutes(30));
        Subtask subtask3 = new Subtask("task", "task", epic1, startTime.plusHours(5), Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(subtask1);
        inMemoryTaskManager.addTask(subtask2);
        inMemoryTaskManager.addTask(subtask3);
        inMemoryTaskManager.getAllSubtaskFromEpic(1);
        inMemoryTaskManager.deleteById(epic1.getId());
        Task[] tasks = {};
        assertArrayEquals(tasks, inMemoryTaskManager.getHistoryManager().getHistory().toArray());
    }

    @Test
    public void cantAddWhenTimeIntersect() {
        inMemoryTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("name", "description", epic1, startTime.plusHours(3), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("name", "description", epic1, startTime.plusHours(2).plusMinutes(45), Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(subtask1);
        assertThrows(IllegalArgumentException.class, () -> inMemoryTaskManager.addTask(subtask2));
    }

    @Test
    public void doesTimeIntersectWith2SameTimeTasks() {
        // inMemoryTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("name", "description", epic1, startTime.plusHours(3), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("name", "description", epic1, startTime.plusHours(2).plusMinutes(45), Duration.ofMinutes(30));

        assertTrue(inMemoryTaskManager.doesTimeIntersect(subtask1, subtask2));
    }

    @Test
    public void doesTimeIntersectWith2DifferentTimeTasks() {
        // inMemoryTaskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("name", "description", epic1, startTime.plusHours(3), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("name", "description", epic1, startTime.plusHours(4).plusMinutes(45), Duration.ofMinutes(30));

        assertFalse(inMemoryTaskManager.doesTimeIntersect(subtask1, subtask2));
    }
}