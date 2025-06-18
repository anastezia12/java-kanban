package main.manager.InMemory;

import main.manager.HistoryManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Map<Integer, Task> tasks;
    private int counter = 1;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
    }

    public void addTask(Task task) {

        task.setId(counter);
        tasks.put(counter, task);
        if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;

            Epic epic = (Epic) tasks.get(subtask.getIdOfEpic());
            epic.addSubtask(subtask);
            epic.updateStatus(tasks);
        }

        historyManager.add(task);
        counter++;
    }

    public void deleteAllTasks() {
        for (int task : tasks.keySet()) {
            if (tasks.get(task).getType() == TaskType.TASK) {
                deleteById(task);
            }
        }
    }

    public void deleteAllSubtasks() {
        for (int task : tasks.keySet()) {
            if (tasks.get(task).getType() == TaskType.SUBTASK) {
                deleteById(task);
            }
        }
    }

    public void deleteAllEpic() {
        for (int task : tasks.keySet()) {
            if (tasks.get(task).getType() == TaskType.EPIC) {
                deleteById(task);
            }
        }
    }

    public void deleteById(int id) {
        if (tasks.containsKey(id)) {
            if (tasks.get(id).getType() == TaskType.EPIC) {
                Epic epic = (Epic) tasks.get(id);
                List<Integer> subtask = epic.getSubtasks();
                for (int idOfSubtask : subtask) {
                    tasks.get(idOfSubtask).setId(0);
                    tasks.remove(idOfSubtask);
                    historyManager.remove(idOfSubtask);
                }
            } else if (tasks.get(id).getType() == TaskType.SUBTASK) {
                Subtask subtask = (Subtask) tasks.get(id);
                Epic epic = (Epic) tasks.get(subtask.getIdOfEpic());
                epic.deleteSubtaskFromEpic(id);
                epic.updateStatus(tasks);
            }
            tasks.get(id).setId(0);
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (task.getType() == TaskType.SUBTASK) {
                Subtask subtask = (Subtask) task;
                Epic epic = (Epic) tasks.get(subtask.getIdOfEpic());
                epic.updateStatus(tasks);
            }

            tasks.put(task.getId(), task);
            historyManager.add(task);
        }
    }

    public Task findById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id).copy());
            return tasks.get(id);
        }
        return null;
    }

    public List<Subtask> getAllSubtaskFromEpic(int idOfEpic) {
        Epic epic = (Epic) tasks.get(idOfEpic);
        List<Subtask> subtasks = new LinkedList<>();
        if (epic != null) {
            for (int idOfSubtasksOfEpic : epic.getSubtasks()) {
                historyManager.add(tasks.get(idOfSubtasksOfEpic).copy());
                subtasks.add((Subtask) tasks.get(idOfSubtasksOfEpic));
            }
        }
        return subtasks;
    }

    public List<Task> getAllTasks() {
        List<Task> task = new LinkedList<>();
        for (Task tasksInManager : tasks.values()) {
            if (tasksInManager.getType() == TaskType.TASK) {
                historyManager.add(tasksInManager.copy());
                task.add(tasksInManager);
            }
        }
        return task;
    }


    public List<Epic> getAllEpic() {
        List<Epic> task = new LinkedList<>();
        for (Task tasksInManager : tasks.values()) {
            if (tasksInManager.getType() == TaskType.EPIC) {
                historyManager.add(tasksInManager.copy());
                task.add((Epic) tasksInManager);
            }
        }
        return task;
    }

    public List<Subtask> getAllSubtasks() {
        List<Subtask> task = new LinkedList<>();
        for (Task tasksInManager : tasks.values()) {
            if (tasksInManager.getType() == TaskType.SUBTASK) {
                historyManager.add(tasksInManager.copy());
                task.add((Subtask) tasksInManager);
            }
        }
        return task;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public List<Task> getListTasks() {
        List<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }


}