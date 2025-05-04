package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }


    @Override
    public void add(Task task) {
        Task taskCopy;
        switch (task.getType()) {
            case TASK:
                taskCopy = new Task(task.getName(), task.getDescription());
                break;
            case EPIC:
                taskCopy = new Epic(task.getName(), task.getDescription());
                break;
            case SUBTASK:
                taskCopy = new Subtask(task.getName(), task.getDescription(), ((Subtask) task).getIdOfEpic());
                break;
            default:
                return;
        }
        taskCopy.setId(task.getId());
        taskCopy.setStatus(task.getStatus());
        history.add(taskCopy);
        while (history.size() > 10) {
            history.removeFirst();
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
