public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Subtask(int id, String name, String description, Epic epic, Status status) {
        super(id, name, description, status);
        this.epic = epic;
        epic.addSubtask(this);

    }

}
