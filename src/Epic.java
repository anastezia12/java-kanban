import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new HashMap<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateStatus();
    }

    public void updateStatus() {
        if (subtasks.isEmpty()) {
            super.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            super.setStatus(Status.DONE);
        } else if (allNew) {
            super.setStatus(Status.NEW);
        } else {
            super.setStatus(Status.IN_PROGRESS);
        }
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }


    @Override
    public String toString() {
        return "Epic{" +
                super.toString() +
                ", subtasks=" + subtasks +
                '}';
    }
}
