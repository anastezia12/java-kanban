import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks;

    public Manager() {
        this.tasks = new HashMap<>();
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void updateTask(Task task) {

        tasks.put(task.getId(), task);

    }

    public Task findById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    public HashMap<Integer, Subtask> getAllSubtaskFromEpic(Task task) {
        Epic epic = (Epic) this.findById(task.getId());
        if (epic != null) {
            HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
            return subtasks;
        }
        return null;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> task = new ArrayList<>();
        for (Task i : tasks.values()) {
            if (i.getClass().equals(Task.class)) {
                task.add(i);
            }
        }
        return task;
    }


    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> task = new ArrayList<>();
        for (Task i : tasks.values()) {
            if (i.getClass().equals(Epic.class)) {
                task.add((Epic) i);
            }
        }
        return task;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> task = new ArrayList<>();
        for (Task i : tasks.values()) {
            if (i.getClass().equals(Subtask.class)) {
                task.add((Subtask) i);
            }
        }
        return task;
    }

    //-------------
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<Integer, Task> tasks) {
        this.tasks = tasks;
    }


}
