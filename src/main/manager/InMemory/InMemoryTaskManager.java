package main.manager.InMemory;

import main.manager.HistoryManager;
import main.manager.Managers;
import main.manager.TaskByTimeComparator;
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
    private Set<Task> taskByTime = new TreeSet<>(new TaskByTimeComparator());

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
    }

    public void addTask(Task task) {
        if (thisTaskIntersect(task)) {
            throw new IllegalArgumentException("Time in the task intersect with another task.");
        }
        task.setId(counter);
        tasks.put(counter, task);
        if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;

            Epic epic = (Epic) tasks.get(subtask.getIdOfEpic());
            epic.updateTime(tasks);
            epic.addSubtask(subtask);
            epic.updateStatus(tasks);

        }

        historyManager.add(task);
        counter++;
        if (task.getStartTime() != null) {
            taskByTime.add(task);
        }
    }

    public void deleteAllTasks() {
        for (int task : tasks.keySet()) {
            if (tasks.get(task).getType() == TaskType.TASK) {
                deleteById(task);
            }
        }
        taskByTime = new TreeSet<>(new TaskByTimeComparator());
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
            taskByTime.remove(tasks.get(id));
            tasks.get(id).setId(0);
            tasks.remove(id);
            historyManager.remove(id);

        }
    }

    public void updateTask(Task task) {
        if (thisTaskIntersect(task)) {
            throw new IllegalArgumentException("Time in the task intersect with another task.");
        }
        if (tasks.containsKey(task.getId())) {
            Task oldTask = tasks.get(task.getId());

            if (oldTask.getStartTime() != null) {
                taskByTime.remove(oldTask);
            }

            if (task.getType() == TaskType.SUBTASK) {
                Subtask subtask = (Subtask) task;
                Epic epic = (Epic) tasks.get(subtask.getIdOfEpic());
                epic.updateStatus(tasks);
                epic.updateTime(tasks);
            }
            tasks.put(task.getId(), task);

            if (task.getStartTime() != null) {
                taskByTime.add(task);
            }
            historyManager.add(task);
        }
    }

    public Task findById(int id) {
        return tasks.values().stream().filter(task -> task.getId() == id).peek(task -> historyManager.add(task.copy())).findFirst().orElse(null);
    }

    public List<Subtask> getAllSubtaskFromEpic(int idOfEpic) {
        Epic epic = (Epic) tasks.get(idOfEpic);
        List<Subtask> subtasks = new LinkedList<>();
        if (epic != null) {
            subtasks = epic.getSubtasks().stream().map(idOfSubtask -> (Subtask) tasks.get(idOfSubtask)).peek(task -> historyManager.add(task.copy())).toList();
        }
        return subtasks;
    }

    public List<Task> getAllTasks() {
        return tasks.values().stream()
                .filter(task -> task.getType() == TaskType.TASK)
                .peek(task -> historyManager.add(task.copy()))
                .map(task -> (Task) task).toList();
    }


    public List<Epic> getAllEpic() {
        return tasks.values().stream()
                .filter(task -> task.getType() == TaskType.EPIC)
                .peek(task -> historyManager.add(task.copy()))
                .map(task -> (Epic) task).toList();
    }

    public List<Subtask> getAllSubtasks() {
        return tasks.values().stream()
                .filter(task -> task.getType() == TaskType.SUBTASK)
                .peek(task -> historyManager.add(task.copy()))
                .map(task -> (Subtask) task).toList();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public List<Task> getListTasks() {
        List<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return taskByTime;
    }

    public boolean doesTimeIntersect(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        return task1.getStartTime().isBefore(task2.getEndTime()) && task2.getStartTime().isBefore(task1.getEndTime());
    }

    public boolean thisTaskIntersect(Task task1) {
        return taskByTime.stream().filter(task -> task.getId() != task1.getId()).anyMatch(task -> doesTimeIntersect(task, task1));

    }
}