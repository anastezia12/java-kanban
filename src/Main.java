public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();
        manager.addTask(new Task("Task 1", "Description task 1 "));
        manager.addTask(new Task("Task 2", " Description task 2 "));
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        manager.addTask(epic1);
        manager.addTask(new Epic("Epic 2", "Description epic 2"));
        Subtask subtask1 = new Subtask("Subtask 1", "description subtask 1", epic1);
        Subtask subtask1changed = new Subtask(subtask1.getId(), " Changed subtask1", " chenged 1 ", epic1, Status.DONE);
        Subtask subtask3 = new Subtask("Subtask 3", "description subtask 3", manager.getAllEpic().get(1));

        Subtask subtask3Changed = new Subtask(subtask3.getId(), "Subtask 3 changed", "description subtask 3changd ", manager.getAllEpic().get(1), Status.DONE);
        manager.addTask(new Subtask("Subtask 2", "description subtask 2", manager.getAllEpic().get(0)));


        Task taskToChange = new Task("task to change", " change it");
        System.out.println(taskToChange);
        Task updatedTask = new Task(taskToChange.getId(), "Changed task", "we already changed it",
                Status.IN_PROGRESS);
        manager.updateTask(updatedTask);
        System.out.println(manager.findById(taskToChange.getId()));
        System.out.println("-------------------");

        System.out.println(manager.getAllEpic().toString());
        manager.deleteById(manager.getAllEpic().get(0).getId());

        System.out.println(manager.getAllEpic().toString());

        System.out.println(manager.getTasks().toString());

        manager.deleteAllTasks();
        System.out.println("Is it deleted " + manager.getTasks().isEmpty());
    }
}
