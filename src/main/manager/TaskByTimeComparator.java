package main.manager;

import main.task.Task;

import java.util.Comparator;

public class TaskByTimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if(task1.getStartTime() == null || task2.getStartTime() == null ){
            throw new IllegalArgumentException("Task with null startTime can`t be sorted");
        }
        return task1.getStartTime().compareTo(task2.getStartTime());
    }
}
