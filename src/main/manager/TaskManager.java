package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task task);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpic();

    void deleteById(int id);

    void updateTask(Task task);

    Task findById(int id);

    ArrayList<Subtask> getAllSubtaskFromEpic(int idOfEpic);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpic();

    ArrayList<Subtask> getAllSubtasks();
}
