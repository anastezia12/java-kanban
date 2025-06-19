package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager managerWithSampleTasks(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
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
        File tempFile = File.createTempFile("text", ".txt");
        tempFile.deleteOnExit();
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        fileBackedTaskManager.getAllEpic();
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getAllTasks());
    }

    @Test
    public void loadFromFileAllTypesOfTasks() throws IOException {
        File tempFile = File.createTempFile("start", ".txt");
        tempFile.deleteOnExit();
        FileBackedTaskManager manager = managerWithSampleTasks(tempFile);

        FileBackedTaskManager managerFromFile = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(manager.getListTasks(), managerFromFile.getListTasks());
    }

    @Test
    public void loadFromFileAllTypesOfTasksWithCorrectIds() throws IOException {
        File tempFile = File.createTempFile("start", ".txt");
        tempFile.deleteOnExit();
        FileBackedTaskManager manager = managerWithSampleTasks(tempFile);
        manager.deleteById(1);
        manager.deleteById(3);
        FileBackedTaskManager managerFromFile = FileBackedTaskManager.loadFromFile(tempFile);
        manager.findById(2).setId(1);
        assertEquals(manager.getListTasks(), managerFromFile.getListTasks());
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