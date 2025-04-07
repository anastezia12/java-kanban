package task;

public class Subtask extends Task {
    private int idOfEpic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.idOfEpic = epic.getId();
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }
}
