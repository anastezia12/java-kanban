package test;

import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
/*I can’t write this test: "проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;",
because an Epic cannot be converted to a Subtask.
And the same for this test:"проверьте, что объект Subtask нельзя сделать своим же эпиком;"
 What should I do?
*/
    /*@Test
    public void cantEpicAsSubtask(){
    Epic epic = new Epic("name", "description");
    Epic epic2 = new Epic("name2", "description2");
    assertThrows(epic.addSubtask(epic2));
}*/

    @Test
    public void StatusForNewEpicIsNew() {
        Epic epic = new Epic("name", " description");
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void updatingStatusToInProgressAfterUpdatingStatusOfSubtask() {
        Epic epic = new Epic("name", " description");
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("name", "description", epic);
        taskManager.addTask(subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subtask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        subtask.setStatus(Status.DONE);
        taskManager.updateTask(subtask);
        assertEquals(Status.DONE, epic.getStatus());
    }
}