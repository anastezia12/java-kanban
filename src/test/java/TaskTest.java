package test.java;

import main.task.Epic;
import main.task.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {
    @Test
    public void sameIdSameTask() {
        Task task1 = new Task("name", "description");
        Task task2 = new Task("name2", " description2");
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2);
    }

    @Test
    public void sameIdSameTaskInInheritedClasses() {
        Epic epic1 = new Epic("name", "description");
        Epic epic2 = new Epic("name2", "description2");
        epic1.setId(2);
        epic2.setId(2);
        assertEquals(epic1, epic2);
    }

    @Test
    public void differentIdDiferentTasks() {
        Task task1 = new Task("Name", "description");
        Task task2 = new Task("name2", "description2");
        task1.setId(1);
        task2.setId(2);
        assertNotEquals(task1, task2);
    }
}