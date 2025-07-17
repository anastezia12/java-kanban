package main.manager;

import main.exceptions.ManagerSaveException;
import main.manager.InMemory.InMemoryTaskManager;
import main.task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static main.data.DateConst.DATE_TIME_FORMATTER;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File saveFile;
    private final DateTimeFormatter timeFormater = DateTimeFormatter.ofPattern("HH:mm");

    public FileBackedTaskManager(File saveFile) {
        this.saveFile = saveFile;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            Map<Integer, Integer> idsOfEpics = new HashMap<>();
            while (bufferedReader.ready()) {
                String[] fields = bufferedReader.readLine().split(", ");
                int id = Integer.parseInt(fields[0]);
                TaskType type = TaskType.valueOf(fields[1]);
                String name = fields[2];
                Status status = Status.valueOf(fields[3]);
                String description = fields[7];
                LocalDateTime startTime = LocalDateTime.parse(fields[4], DATE_TIME_FORMATTER);
                Duration duration = Duration.ofMinutes(Integer.parseInt(fields[5]));

                switch (type) {
                    case TASK:
                        Task task = new Task(id, name, description, status, startTime, duration);
                        manager.addTask(task);
                        break;
                    case EPIC:
                        Epic epic = new Epic(name, description);
                        manager.addTask(epic);
                        idsOfEpics.put(id, epic.getId());
                        break;
                    case SUBTASK:
                        int epicId = idsOfEpics.get(Integer.parseInt(fields[8]));
                        Subtask subtask = new Subtask(id, name, description, status, epicId, startTime, duration);
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
            writer.write("id, type, name, status, start time,duration, end time, description, epic\n");
            super.getListTasks().forEach(i -> {
                try {
                    writer.write(taskInLineForSaving(i));
                } catch (IOException e) {
                    throw new RuntimeException("Ошибка при сохранении данных в файл", e);
                }
            });

        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл", exception);
        }
    }

    private String taskInLineForSaving(Task task) {
        String startTimeStr = Optional.ofNullable(task.getStartTime()).map(t -> t.format(DATE_TIME_FORMATTER)).orElse("");

        String endTime = Optional.ofNullable(task.getEndTime()).map(t -> t.format(DATE_TIME_FORMATTER)).orElse("");

        StringBuilder newTaskToWrite = new StringBuilder();
        newTaskToWrite.append(task.getId()).append(", ")
                .append(task.getType()).append(", ")
                .append(task.getName()).append(", ")
                .append(task.getStatus()).append(", ")
                .append(startTimeStr).append(", ")
                .append(task.getDuration().toMinutes()).append(", ")
                .append(endTime).append(", ")
                .append(task.getDescription());
        if (task.getType() == TaskType.SUBTASK) {

            newTaskToWrite.append(", ").append(((Subtask) task).getIdOfEpic());
        }
        newTaskToWrite.append("\n");

        return newTaskToWrite.toString();
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
