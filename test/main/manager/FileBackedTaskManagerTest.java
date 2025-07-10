package main.manager;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    protected FileBackedTaskManager createTaskManager() throws IOException {
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();
        return new FileBackedTaskManager(tempFile);
    }

    @Override
    protected HistoryManager createHistoryManager() {
        return taskManager.getHistoryManager();
    }

    private FileBackedTaskManager managerWithSampleTasks(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.addTask(task1);
        manager.addTask(epic1);
        oneSubtask();
        manager.addTask(subtask1);
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
        manager.addTask(task1);
        manager.addTask(epic1);
        oneSubtask();
        manager.addTask(subtask1);
        assertEquals(List.of(task1, epic1, subtask1), manager.getListTasks());
        assertEquals(List.of(task1), manager.getAllTasks());
        assertEquals(List.of(epic1), manager.getAllEpic());
        assertEquals(List.of(subtask1), manager.getAllSubtasks());
    }


}