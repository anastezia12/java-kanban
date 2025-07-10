package main.manager;

import main.manager.InMemory.InMemoryTaskManager;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.Test;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Override
    protected HistoryManager createHistoryManager() {
        return taskManager.getHistoryManager();
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