package main.manager;

import main.exceptions.ManagerSaveException;
import main.manager.InMemory.InMemoryTaskManager;
import main.task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {
            br.readLine();
            Map<Integer, Integer> idsOfEpics = new HashMap<>();
            while (br.ready()) {
                String[] ar = br.readLine().split(",");
                int id = Integer.parseInt(ar[0]);
                TaskType type = TaskType.valueOf(ar[1]);
                String name = ar[2];
                Status status = Status.valueOf(ar[3]);
                String description = ar[4];
                switch (type) {
                    case TASK:
                        Task task = new Task(id, name, description, status);
                        manager.addTask(task);
                        break;
                    case EPIC:
                        Epic epic = new Epic(name, description);
                        manager.addTask(epic);
                        idsOfEpics.put(id, epic.getId());
                        break;
                    case SUBTASK:
                        int epicId = idsOfEpics.get(Integer.parseInt(ar[5]));
                        Subtask subtask = new Subtask(id, name, description, status, epicId);
                        manager.addTask(subtask);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        return manager;
    }

    public static void main(String[] args) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        Task task1 = new Task("Task1", "Description task1");
        Task task2 = new Task("Task2", "Description task2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Epic1", "Description epic1");
        Epic epic2 = new Epic("Epic2", "Description epic2");
        taskManager.addTask(epic1);
        taskManager.addTask(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "Description subtask1", epic1);
        Subtask subtask2 = new Subtask("Subtask2", "Description subtask2", epic1);
        Subtask subtask3 = new Subtask("Subtask3", "Description subtask3", epic1);
        taskManager.addTask(subtask3);
        taskManager.addTask(subtask1);
        taskManager.addTask(subtask2);

        System.out.println("All tasks:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All history:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }

        System.out.println("All epics:");
        for (Epic epic : taskManager.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("All history:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }

        System.out.println("All subtasks:");
        for (Subtask sub : taskManager.getAllSubtasks()) {
            System.out.println(sub);
        }

        System.out.println("All history:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }

        task1.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.DONE);

        taskManager.updateTask(task1);
        taskManager.updateTask(subtask1);
        taskManager.updateTask(subtask2);
        taskManager.updateTask(subtask3);

        System.out.println("All history after updating:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }
        System.out.println("After status updates:");
        System.out.println("Task1: " + taskManager.findById(task1.getId()));
        System.out.println("Epic1: " + taskManager.findById(epic1.getId()));
        System.out.println("Epic2: " + taskManager.findById(epic2.getId()));

        System.out.println("All history:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }

        taskManager.deleteById(task2.getId());
        taskManager.deleteById(epic1.getId());

        System.out.println("After deletions:");
        System.out.println("All tasks:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All epics:");
        for (Epic epic : taskManager.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("All subtasks:");
        for (Subtask sub : taskManager.getAllSubtasks()) {
            System.out.println(sub);
        }


    }

    private void save() {
        try (FileWriter writer = new FileWriter("Tasks.txt", StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task i : super.getListTasks()) {
                writer.write(i.getId() + "," + i.getType() + "," + i.getName() + "," + i.getStatus() + "," + i.getDescription());
                if (i.getType() == TaskType.SUBTASK) {
                    writer.write("," + ((Subtask) i).getIdOfEpic());
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл", e);
        }
    }

    public void readTasksFile() throws ManagerSaveException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("Tasks.txt"), StandardCharsets.UTF_8))) {
            while (br.ready()) {
                System.out.println(br.readLine());
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteById(int id) {
        super.deleteById(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task findById(int id) {
        return super.findById(id);
    }

    @Override
    public List<Subtask> getAllSubtaskFromEpic(int idOfEpic) {
        return super.getAllSubtaskFromEpic(idOfEpic);

    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public List<Epic> getAllEpic() {
        return super.getAllEpic();
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return super.getAllSubtasks();

    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();
    }

    @Override
    public List<Task> getListTasks() {
        return super.getListTasks();
    }
}
