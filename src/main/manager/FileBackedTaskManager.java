package main.manager;

import main.exceptions.ManagerSaveException;
import main.manager.InMemory.InMemoryTaskManager;
import main.task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File saveFile;

    public FileBackedTaskManager(File saveFile) {
        this.saveFile = saveFile;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            Map<Integer, Integer> idsOfEpics = new HashMap<>();
            while (bufferedReader.ready()) {
                String[] fields = bufferedReader.readLine().split(",");
                int id = Integer.parseInt(fields[0]);
                TaskType type = TaskType.valueOf(fields[1]);
                String name = fields[2];
                Status status = Status.valueOf(fields[3]);
                String description = fields[4];
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
                        int epicId = idsOfEpics.get(Integer.parseInt(fields[5]));
                        Subtask subtask = new Subtask(id, name, description, status, epicId);
                        manager.addTask(subtask);
                        break;
                }
            }
        } catch (IOException exception) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        return manager;
    }

    private void save() {
        try (FileWriter writer = new FileWriter(saveFile, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task i : super.getListTasks()) {
                writer.write(i.getId() + "," + i.getType() + "," + i.getName() + "," + i.getStatus() + "," + i.getDescription());
                if (i.getType() == TaskType.SUBTASK) {
                    writer.write("," + ((Subtask) i).getIdOfEpic());
                }
                writer.write("\n");
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл", exception);
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

}
