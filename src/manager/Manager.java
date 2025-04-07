package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks;
    private int counter = 1;

    public Manager() {
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
                    tasks.remove(idOfSubtask);
                }
            } else if (tasks.get(id).getType() == TaskType.SUBTASK) {
                Subtask subtask = (Subtask) tasks.get(id);
                Epic epic = (Epic) tasks.get(subtask.getIdOfEpic());
                ArrayList<Integer> subtasksOfEpic = epic.getSubtasks();
                subtasksOfEpic.remove(id);
                epic.setSubtasks(subtasksOfEpic);
                epic.updateStatus(tasks);
            }
            tasks.remove(id);
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
        }
    }

    public Task findById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    public ArrayList<Subtask> getAllSubtaskFromEpic(int idOfEpic) {
        Epic epic = (Epic) tasks.get(idOfEpic);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        if (epic != null) {
            for (int idOfSubtasksOfEpic : epic.getSubtasks()) {
                subtasks.add((Subtask) tasks.get(idOfSubtasksOfEpic));
            }
            return subtasks;
        }
        return null;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> task = new ArrayList<>();
        for (Task tasksInManager : tasks.values()) {
            if (tasksInManager.getType() == TaskType.TASK) {
                task.add(tasksInManager);
            }
        }
        return task;
    }


    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> task = new ArrayList<>();
        for (Task tasksInManager : tasks.values()) {
            if (tasksInManager.getType() == TaskType.EPIC) {
                task.add((Epic) tasksInManager);
            }
        }
        return task;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> task = new ArrayList<>();
        for (Task tasksInManager : tasks.values()) {
            if (tasksInManager.getType() == TaskType.SUBTASK) {
                task.add((Subtask) tasksInManager);
            }
        }
        return task;
    }

    public void setTasks(HashMap<Integer, Task> tasks) {
        this.tasks = tasks;
    }
}