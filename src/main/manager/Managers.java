package main.manager;

import main.manager.InMemory.InMemoryHistoryManager;

import java.io.File;


public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("Tasks.txt"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
