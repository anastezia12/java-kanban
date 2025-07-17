package WebTest;

import main.task.Status;
import main.task.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;
import static org.junit.jupiter.api.Assertions.*;

public class TasksHandlerTest extends BaseHttpTest {
    private final Task task1 = new Task("name", "description", START_TIME, THIRTY_MINUTES);

    public TasksHandlerTest() throws IOException {
        super(URI.create("http://localhost:8080/tasks"));
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {

        HttpResponse<String> addResp = addTask(task1);
        assertEquals(201, addResp.statusCode());
        List<Task> tasksFromManager = manager.getListTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("name", tasksFromManager.get(0).getName(), "Некорректное имя задачи");

    }

    @Test
    public void shouldGetOneTaskWhenPutOne() throws IOException, InterruptedException {
        HttpResponse<String> response = addTask(task1);
        assertTrue(response.body().contains("name"));
        assertTrue(response.body().contains("description"));
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        testGetMethodWhenOneTask(task1);
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task createdTask = gson.fromJson(addTask(task1).body(), Task.class);
        createdTask.setStatus(Status.IN_PROGRESS);
        HttpResponse<String> response2 = addTask(createdTask);

        assertEquals(201, response2.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals(Status.IN_PROGRESS, tasksFromManager.get(0).getStatus());
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task created = gson.fromJson(addTask(task1).body(), Task.class);
        HttpResponse<String> deleteResp = deleteTask(created.getId());

        assertEquals(200, deleteResp.statusCode());
        assertEquals("deleted", deleteResp.body());
        System.out.println(manager.getAllTasks());
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void with2TasksWithSameTime() throws IOException, InterruptedException {
        addTask(task1);
        Task task2 = new Task("name2", " description2", START_TIME.plusMinutes(10), THIRTY_MINUTES);
        HttpResponse<String> response = addTask(task2);
        assertEquals(406, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals(task1.getName(), tasksFromManager.get(0).getName());
        assertFalse(response.body().contains(task2.getName()));
    }
}
