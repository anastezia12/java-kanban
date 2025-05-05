package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpic();

    void deleteById(int id);

    void updateTask(Task task);

    Task findById(int id);

    List<Subtask> getAllSubtaskFromEpic(int idOfEpic);

    List<Task> getAllTasks();

    List<Epic> getAllEpic();

    List<Subtask> getAllSubtasks();

    HistoryManager getHistoryManager();
}
