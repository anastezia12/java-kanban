package main.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int idOfEpic;

    public Subtask(String name, String description, Epic epic, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        idOfEpic = epic.getId();
    }

    public Subtask(int id, String name, String description, Status status, int epic, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        idOfEpic = epic;
    }

    public Subtask(Subtask subtask) {
        super(subtask.getId(), subtask.getName(), subtask.getDescription(), subtask.getStatus(), subtask.getStartTime(), subtask.getDuration());
        idOfEpic = subtask.idOfEpic;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }

    @Override
    public Subtask copy() {
        return new Subtask(this);
    }
}
