package test.java;

import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    public void statusForNewEpicIsNew() {
        Epic epic = new Epic("name", " description");
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void updatingStatusToInProgressAfterAddingSubtask() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic);
        taskManager.addTask(subtask);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void updatingStatusToInProgressAfterUpdatingStatusOfSubtaskToInProgress() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic);
        taskManager.addTask(subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subtask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void updatingStatusToInProgressAfterUpdatingStatusOfSubtask() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic);
        taskManager.addTask(subtask);
        subtask.setStatus(Status.DONE);
        taskManager.updateTask(subtask);
        assertEquals(Status.DONE, epic.getStatus());
    }
}