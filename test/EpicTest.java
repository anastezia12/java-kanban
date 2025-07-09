import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
    private LocalDateTime startTime = LocalDateTime.parse("11:12 12.10.24", dateTimeFormatter);

    @Test
    public void statusForNewEpicIsNew() {
        Epic epic = new Epic("name", " description");
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void notUpdatingStatusToInProgressAfterAddingSubtask() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic, startTime, Duration.ofMinutes(30));
        taskManager.addTask(subtask);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void updatingStatusToInProgressAfterUpdatingStatusOfSubtaskToInProgress() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic, startTime, Duration.ofMinutes(20));
        taskManager.addTask(subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subtask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void updatingStatusToDoneAfterUpdatingStatusOfSubtask() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic, startTime, Duration.ofMinutes(30));
        taskManager.addTask(subtask);
        subtask.setStatus(Status.DONE);
        taskManager.updateTask(subtask);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void updatingStatusToInProgressAfterUpdatingStatusOfSubtasksToInProgressAndDone() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic, startTime, Duration.ofMinutes(30));
        taskManager.addTask(subtask);
        subtask.setStatus(Status.DONE);
        taskManager.updateTask(subtask);
        Subtask subtask2 = new Subtask("name2", "description2", epic, startTime.plusHours(1), Duration.ofMinutes(30));
        taskManager.addTask(subtask2);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void durationAdnStartTimeForEpicIsSameWithDurationOfSubtask() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic, startTime, Duration.ofMinutes(30));
        taskManager.addTask(subtask);
        assertEquals(Duration.ofMinutes(30), epic.getDuration());
        assertEquals(startTime, epic.getStartTime());
    }

    @Test
    public void durationOfEpicIsAddingDurationOfSubtasks() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic, startTime, Duration.ofMinutes(30));
        taskManager.addTask(subtask);
        Subtask subtask1 = new Subtask("name", "description", epic, startTime.plusHours(1), Duration.ofMinutes(30));
        taskManager.addTask(subtask1);
        assertEquals(Duration.ofHours(1), epic.getDuration());
    }

    @Test
    public void StartTimeOfEpicIsEarliestStartTimeOfSubtasks() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic, startTime.plusDays(2), Duration.ofMinutes(30));
        taskManager.addTask(subtask);
        Subtask subtask1 = new Subtask("name", "description", epic, startTime.plusHours(1), Duration.ofMinutes(30));
        taskManager.addTask(subtask1);
        assertEquals(startTime.plusHours(1), epic.getStartTime());
    }

    @Test
    public void SendTimeOfEpicIsLatestTimeOfSubtasks() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic, startTime.plusDays(2), Duration.ofMinutes(30));
        taskManager.addTask(subtask);
        Subtask subtask1 = new Subtask("name", "description", epic, startTime.plusHours(1), Duration.ofMinutes(30));
        taskManager.addTask(subtask1);
        assertEquals(startTime.plusMinutes(30).plusDays(2), epic.getEndTime());
    }
}