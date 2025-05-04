package main.task;

public class Subtask extends Task {
    private int idOfEpic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        idOfEpic = epic.getId();
    }

    public Subtask(String name, String description, int idOfEpic) {
        super(name, description);
        this.idOfEpic = idOfEpic;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }

}
