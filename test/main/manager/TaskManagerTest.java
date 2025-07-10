package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;


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
    protected HistoryManager historyManager;

    protected abstract T createTaskManager() throws IOException;

    protected abstract HistoryManager createHistoryManager();

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = createTaskManager();
        historyManager = createHistoryManager();
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

}
