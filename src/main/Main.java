package main;

import main.manager.InMemoryTaskManager;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import main.task.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("Task1", "Description task1");
        Task task2 = new Task("Task2", "Description task2");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);

        Epic epic1 = new Epic("Epic1", "Description epic1");
        Epic epic2 = new Epic("Epic2", "Description epic2");
        inMemoryTaskManager.addTask(epic1);

        Subtask subtask1 = new Subtask("Subtask1", "Description subtask1", epic1);
        Subtask subtask2 = new Subtask("Subtask2", "Description subtask2", epic1);
        inMemoryTaskManager.addTask(subtask1);
        inMemoryTaskManager.addTask(subtask2);

        inMemoryTaskManager.addTask(epic2);
        Subtask subtask3 = new Subtask("Subtask3", "Description subtask3", epic2);
        inMemoryTaskManager.addTask(subtask3);


        System.out.println("All tasks:");
        for (Task task : inMemoryTaskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All epics:");
        for (Epic epic : inMemoryTaskManager.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("All subtasks:");
        for (Subtask sub : inMemoryTaskManager.getAllSubtasks()) {
            System.out.println(sub);
        }

        task1.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.DONE);

        inMemoryTaskManager.updateTask(task1);
        inMemoryTaskManager.updateTask(subtask1);
        inMemoryTaskManager.updateTask(subtask2);
        inMemoryTaskManager.updateTask(subtask3);

        System.out.println("After status updates:");
        System.out.println("Task1: " + inMemoryTaskManager.findById(task1.getId()));
        System.out.println("Epic1: " + inMemoryTaskManager.findById(epic1.getId()));
        System.out.println("Epic2: " + inMemoryTaskManager.findById(epic2.getId()));

        inMemoryTaskManager.deleteById(task2.getId());
        inMemoryTaskManager.deleteById(epic1.getId());

        System.out.println("After deletions:");
        System.out.println("All tasks:");
        for (Task task : inMemoryTaskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All epics:");
        for (Epic epic : inMemoryTaskManager.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("All subtasks:");
        for (Subtask sub : inMemoryTaskManager.getAllSubtasks()) {
            System.out.println(sub);
        }
    }
}
