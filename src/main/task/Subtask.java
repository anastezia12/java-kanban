package main.task;

public class Subtask extends Task {
    private int idOfEpic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        idOfEpic = epic.getId();
    }

    public Subtask(Subtask subtask) {
        super(subtask.getId(), subtask.getName(), subtask.getDescription(), subtask.getStatus());
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
