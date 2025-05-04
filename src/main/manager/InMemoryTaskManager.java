package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private HashMap<Integer, Task> tasks;
    private int counter = 1;

    public InMemoryTaskManager() {
        super();
        this.tasks = new HashMap<>();
    }

    public void addTask(Task task) {
        tasks.put(counter, task);
        task.setId(counter);
        if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;

            Epic epic = (Epic) tasks.get(subtask.getIdOfEpic());
            epic.addSubtask(subtask);
            epic.updateStatus(tasks);
        }
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
                ArrayList<Integer> subtask = epic.getSubtasks();
                for (int idOfSubtask : subtask) {
                    tasks.remove(Integer.valueOf(idOfSubtask));
                }
            } else if (tasks.get(id).getType() == TaskType.SUBTASK) {
                Subtask subtask = (Subtask) tasks.get(id);
                Epic epic = (Epic) tasks.get(subtask.getIdOfEpic());
                epic.deleteSubtaskFromEpic(id);
                epic.updateStatus(tasks);
            }
            tasks.remove(Integer.valueOf(id));
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
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    public ArrayList<Subtask> getAllSubtaskFromEpic(int idOfEpic) {
        Epic epic = (Epic) tasks.get(idOfEpic);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        if (epic != null) {
            for (int idOfSubtasksOfEpic : epic.getSubtasks()) {

                historyManager.add(tasks.get(idOfSubtasksOfEpic));
                subtasks.add((Subtask) tasks.get(idOfSubtasksOfEpic));
            }
        }
        return subtasks;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> task = new ArrayList<>();
        for (Task tasksInManager : tasks.values()) {
            if (tasksInManager.getType() == TaskType.TASK) {

                historyManager.add(tasksInManager);
                task.add(tasksInManager);
            }
        }
        return task;
    }


    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> task = new ArrayList<>();
        for (Task tasksInManager : tasks.values()) {
            if (tasksInManager.getType() == TaskType.EPIC) {

                historyManager.add(tasksInManager);
                task.add((Epic) tasksInManager);
            }
        }
        return task;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> task = new ArrayList<>();
        for (Task tasksInManager : tasks.values()) {
            if (tasksInManager.getType() == TaskType.SUBTASK) {

                historyManager.add(tasksInManager);
                task.add((Subtask) tasksInManager);
            }
        }
        return task;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

}