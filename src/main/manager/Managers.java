package main.manager;

import main.manager.InMemory.InMemoryHistoryManager;
import main.manager.InMemory.InMemoryTaskManager;


public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
