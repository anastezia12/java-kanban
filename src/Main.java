import manager.Manager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Task1", "Description task1");
        Task task2 = new Task("Task2", "Description task2");
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic("Epic1", "Description epic1");
        Epic epic2 = new Epic("Epic2", "Description epic2");
        manager.addTask(epic1);

        Subtask subtask1 = new Subtask("Subtask1", "Description subtask1", epic1);
        Subtask subtask2 = new Subtask("Subtask2", "Description subtask2", epic1);
        manager.addTask(subtask1);
        manager.addTask(subtask2);

        manager.addTask(epic2);
        Subtask subtask3 = new Subtask("Subtask3", "Description subtask3", epic2);
        manager.addTask(subtask3);


        System.out.println("All tasks:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All epics:");
        for (Epic epic : manager.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("All subtasks:");
        for (Subtask sub : manager.getAllSubtasks()) {
            System.out.println(sub);
        }

        task1.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.DONE);

        manager.updateTask(task1);
        manager.updateTask(subtask1);
        manager.updateTask(subtask2);
        manager.updateTask(subtask3);

        System.out.println("After status updates:");
        System.out.println("Task1: " + manager.findById(task1.getId()));
        System.out.println("Epic1: " + manager.findById(epic1.getId()));
        System.out.println("Epic2: " + manager.findById(epic2.getId()));

        manager.deleteById(task2.getId());
        manager.deleteById(epic1.getId());

        System.out.println("After deletions:");
        System.out.println("All tasks:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All epics:");
        for (Epic epic : manager.getAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("All subtasks:");
        for (Subtask sub : manager.getAllSubtasks()) {
            System.out.println(sub);
        }
    }
}
