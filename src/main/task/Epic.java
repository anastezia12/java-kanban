package main.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Epic extends Task {
    private List<Integer> subtasksId;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, null, Duration.ZERO);
        subtasksId = new LinkedList<>();
    }

    public Epic(Epic epic) {
        super(epic.getId(), epic.getName(), epic.getDescription(), epic.getStatus(), epic.getStartTime(), epic.getDuration());
        subtasksId = epic.subtasksId;
    }

    public void addSubtask(Subtask subtask) {
        subtasksId.add(subtask.getId());
    }

    public void updateTime(Map<Integer, Task> tasks) {
        LocalDateTime earliest = null;
        LocalDateTime latest = null;
        Duration totalDuration = Duration.ZERO;

        for (Integer subtaskId : subtasksId) {
            Task task = tasks.get(subtaskId);
            if (task != null && task.getType() == TaskType.SUBTASK) {
                LocalDateTime subStart = task.getStartTime();
                LocalDateTime subEnd = task.getEndTime();
                Duration subDuration = task.getDuration();

                if (subStart != null && (earliest == null || subStart.isBefore(earliest))) {
                    earliest = subStart;
                }

                if (subEnd != null && (latest == null || subEnd.isAfter(latest))) {
                    latest = subEnd;
                }

                if (subDuration != null) {
                    totalDuration = totalDuration.plus(subDuration);
                }
            }
        }

        super.setStartTime(earliest);
        this.endTime = latest;
        super.setDuration(totalDuration);
    }

    public void updateStatus(Map<Integer, Task> tasks) {
        if (subtasksId.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (int subtaskId : subtasksId) {
            Task task = tasks.get(subtaskId);
            if (task != null && task.getType() == TaskType.SUBTASK) {
                Status status = task.getStatus();
                if (status != Status.NEW) {
                    allNew = false;
                }
                if (status != Status.DONE) {
                    allDone = false;
                }
            }
        }

        if (allDone) {
            setStatus(Status.DONE);
        } else if (allNew) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
        updateTime(tasks);
    }

    public List<Integer> getSubtasks() {
        return subtasksId;
    }

    public void setSubtasks(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void deleteSubtaskFromEpic(int id) {
        subtasksId.remove(Integer.valueOf(id));
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "main.task.Epic{" + super.toString() + ", subtasks=" + subtasksId + '}';
    }

    @Override
    public Epic copy() {
        return new Epic(this);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }


}
