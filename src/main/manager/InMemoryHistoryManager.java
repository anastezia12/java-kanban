package main.manager;

import main.task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history;

    public InMemoryHistoryManager() {
        history = new LinkedList<>();
    }


    @Override
    public void add(Task task) {//
        history.add(task);
        while (history.size() > MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }
}
