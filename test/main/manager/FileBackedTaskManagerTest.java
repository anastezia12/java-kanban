package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    private File createTempFileWithLines(List<String> data) throws IOException {
        File tempFile = File.createTempFile("start", ".txt");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (String datum : data) {
                writer.write(datum + "\n");
            }
        }
        return tempFile;
    }

    private FileBackedTaskManager managerWithSampleTasks() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(File.createTempFile("Text", ".txt"));
        Task task = new Task("task1", "desc task 1");
        manager.addTask(task);
        Epic epic = new Epic("epic1", "epic1");
        manager.addTask(epic);
        Subtask subtask = new Subtask("sub1", "desc sub1", epic);
        manager.addTask(subtask);
        return manager;
    }

    @Test
    public void loadFromFileWhenEmpty() throws IOException {
        File tempFile = createTempFileWithLines(new ArrayList<>());
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        fileBackedTaskManager.getAllEpic();
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getAllTasks());
    }

    @Test
    public void loadFromFileAllTypesOfTasks() throws IOException {
        List<String> expected = List.of(
                "1,TASK,Task1,DONE,Description task1 from downloaded",
                "2,EPIC,Epic2,DONE,Description epic2 downloaded",
                "3,SUBTASK,Subtask3,DONE,Description subtask3,2"
        );
        File tempFile = createTempFileWithLines(expected);
        FileBackedTaskManager managerFromFile = FileBackedTaskManager.loadFromFile(tempFile);
        managerFromFile.getAllTasks();
        FileBackedTaskManager managerExpected = managerWithSampleTasks();
        assertEquals(managerExpected.getListTasks(), managerFromFile.getListTasks());
    }

    @Test
    public void loadFromFileAllTypesOfTasksWithCorrectIds() throws IOException {
        List<String> given = List.of(
                "12,TASK,Task1,DONE,Description task1 from downloaded",
                "22,EPIC,Epic2,DONE,Description epic2 downloaded",
                "32,SUBTASK,Subtask3,DONE,Description subtask3,22"
        );
        File tempFile = createTempFileWithLines(given);

        FileBackedTaskManager managerFromFile = FileBackedTaskManager.loadFromFile(tempFile);
        managerFromFile.getAllTasks();

        FileBackedTaskManager fileBackedTaskManagerExpected = managerWithSampleTasks();
        assertEquals(fileBackedTaskManagerExpected.getListTasks(), managerFromFile.getListTasks());
    }

    @Test
    public void saveOfFewTasks() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(File.createTempFile("Text", ".txt"));
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