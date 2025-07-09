package main;

import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        LocalDateTime startTime = LocalDateTime.parse("11:12 12.10.24", dateTimeFormatter);
        Task task1 = new Task("Task1", "Description task1", startTime, Duration.ofMinutes(60));
        Task task2 = new Task("Task2", "Description task2", startTime.plusDays(1),Duration.ofMinutes(120) );
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Epic1", "Description epic1");
        Epic epic2 = new Epic("Epic2", "Description epic2");
        taskManager.addTask(epic1);
        taskManager.addTask(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "Description subtask1", epic1, startTime.plusDays(2), Duration.ofMinutes(60));
        Subtask subtask2 = new Subtask("Subtask2", "Description subtask2", epic1, startTime.plusDays(3), Duration.ofMinutes(60));
        Subtask subtask3 = new Subtask("Subtask3", "Description subtask3", epic1, startTime.plusDays(4), Duration.ofMinutes(60));
        taskManager.addTask(subtask3);
        taskManager.addTask(subtask1);
        taskManager.addTask(subtask2);

        System.out.println("All tasks:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All history:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }

        System.out.println("All epics:");
        for (Epic epic : taskManager.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("All history:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }

        System.out.println("All subtasks:");
        for (Subtask sub : taskManager.getAllSubtasks()) {
            System.out.println(sub);
        }

        System.out.println("All history:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }

        task1.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.DONE);

        taskManager.updateTask(task1);
        taskManager.updateTask(subtask1);
        taskManager.updateTask(subtask2);
        taskManager.updateTask(subtask3);

        System.out.println("All history after updating:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }
        System.out.println("After status updates:");
        System.out.println("Task1: " + taskManager.findById(task1.getId()));
        System.out.println("Epic1: " + taskManager.findById(epic1.getId()));
        System.out.println("Epic2: " + taskManager.findById(epic2.getId()));

        System.out.println("All history:");
        for (Task task : taskManager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }

        taskManager.deleteById(task2.getId());
        taskManager.deleteById(epic1.getId());

        System.out.println("After deletions:");
        System.out.println("All tasks:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All epics:");
        for (Epic epic : taskManager.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("All subtasks:");
        for (Subtask sub : taskManager.getAllSubtasks()) {
            System.out.println(sub);
        }


        System.out.println("All prioritized asks:");
        for (Task sub : taskManager.getPrioritizedTasks()) {
            System.out.println(sub);
        }

    }
}
