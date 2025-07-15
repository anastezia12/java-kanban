package main;

import main.WebServices.HttpTaskServer;
import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("name", "description", LocalDateTime.of(2025, 9, 28, 12, 0), Duration.ofMinutes(30));
        Epic epic1 = new Epic("name", "description");
        taskManager.addTask(task1);
        taskManager.addTask(epic1);
        Subtask subtask1 = new Subtask("subtask", "desc", epic1, LocalDateTime.of(2025, 9, 28, 11, 0), Duration.ofMinutes(30));
        taskManager.addTask(subtask1);
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }
}
