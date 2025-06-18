package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    @Test
    public void loadFromFileWhenEmpty() throws IOException {
        File tempFile = File.createTempFile("start.txt", null);
        tempFile.deleteOnExit();
        if (tempFile.exists()) {
            FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
            fileBackedTaskManager.getAllEpic();
            assertEquals(new ArrayList<>(), fileBackedTaskManager.getAllTasks());
        }
    }

    @Test
    public void loadFromFileAllTypesOfTasks() throws IOException {
        File tempFile = File.createTempFile("start", ".txt");
        tempFile.deleteOnExit();
        List<String> expected = List.of(
                "id,type,name,status,description,epic",
                "1,TASK,Task1,DONE,Description task1 from downloaded",
                "2,EPIC,Epic2,DONE,Description epic2 downloaded",
                "3,SUBTASK,Subtask3,DONE,Description subtask3,2"
        );
        try (FileWriter writer = new FileWriter(tempFile, StandardCharsets.UTF_8)) {
            for (int i = 0; i < expected.size(); i++) {
                writer.write(expected.get(i) + "\n");
            }
        }
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);
        manager.getAllTasks();
        List<String> actual = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Tasks.txt", StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                actual.add(line);
            }
        }
        assertEquals(expected, actual);
    }

    @Test
    public void loadFromFileAllTypesOfTasksWithCorrectIds() throws IOException {
        File tempFile = File.createTempFile("start", ".txt");
        tempFile.deleteOnExit();
        List<String> given = List.of(
                "id,type,name,status,description,epic",
                "12,TASK,Task1,DONE,Description task1 from downloaded",
                "22,EPIC,Epic2,DONE,Description epic2 downloaded",
                "32,SUBTASK,Subtask3,DONE,Description subtask3,22"
        );
        try (FileWriter writer = new FileWriter(tempFile, StandardCharsets.UTF_8)) {
            for (int i = 0; i < given.size(); i++) {
                writer.write(given.get(i) + "\n");
            }
        }
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);
        manager.getAllTasks();
        List<String> actual = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Tasks.txt", StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                actual.add(line);
            }
        }
        List<String> expected = List.of(
                "id,type,name,status,description,epic",
                "1,TASK,Task1,DONE,Description task1 from downloaded",
                "2,EPIC,Epic2,DONE,Description epic2 downloaded",
                "3,SUBTASK,Subtask3,DONE,Description subtask3,2"
        );
        assertEquals(expected, actual);
    }

    @Test
    public void saveOfFewTasks() {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        Task task = new Task("task1", "desc task 1");
        manager.addTask(task);
        Epic epic = new Epic("epic1", "epic1");
        manager.addTask(epic);
        Subtask subtask = new Subtask("sub1", "desc sub1", epic);
        manager.addTask(subtask);
        assertEquals(List.of(task, epic, subtask), manager.getListTasks());
        assertEquals(List.of(task), manager.getAllTasks());
        assertEquals(List.of(epic), manager.getAllEpic());
        assertEquals(List.of(subtask), manager.getAllSubtasks());
    }
}