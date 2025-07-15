package WebTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.WebServices.HttpTaskServer;
import main.manager.InMemory.InMemoryTaskManager;
import main.manager.TaskManager;
import main.task.Status;
import main.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;
import static org.junit.jupiter.api.Assertions.*;

public class TasksHandlerTest {
    private final Task task1 = new Task("name", "description", START_TIME, THIRTY_MINUTES);

    private final TaskManager manager = new InMemoryTaskManager();
    private final URI url = URI.create("http://localhost:8080/tasks");
    private final HttpTaskServer taskServer = new HttpTaskServer(manager);
    private final Gson gson = HttpTaskServer.getGson();

    public TasksHandlerTest() throws IOException {
    }

    public static String jsonOfTask(Task task) {
        Gson gson = HttpTaskServer.getGson();

        JsonObject jsonObject = gson.toJsonTree(task).getAsJsonObject();
        jsonObject.addProperty("type", task.getType().toString());

        return gson.toJson(jsonObject);
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpic();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {

        String taskJson = jsonOfTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("name", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        testAddTask();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("name", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        String taskJson = jsonOfTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task createdTask = gson.fromJson(response1.body(), Task.class);
        task1.setId(createdTask.getId());
        task1.setStatus(Status.IN_PROGRESS);
        String taskJsonAfter = jsonOfTask(task1);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonAfter))
                .build();

        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertTrue(response2.statusCode() == 201);
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals(Status.IN_PROGRESS, tasksFromManager.get(0).getStatus());

    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        String taskJson = jsonOfTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task createdTask = gson.fromJson(response1.body(), Task.class);
        task1.setId(createdTask.getId());
        task1.setStatus(Status.IN_PROGRESS);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url + "/1"))
                .DELETE()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager);
        assertEquals(0, tasksFromManager.size());
        assertEquals("deleted", response2.body());
    }

    @Test
    public void afterGettingWithIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url + "/1"))
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response2.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager);
        assertEquals(0, tasksFromManager.size());
        assertEquals("No task with this id", response2.body());
    }

    @Test
    public void deletingWithIncorrectIdGivesCorrectAns() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url + "/1"))
                .DELETE()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager);
        assertEquals(0, tasksFromManager.size());
        assertEquals("deleted", response2.body());
    }

    @Test
    public void with2TasksWithSameTime() throws IOException, InterruptedException {
        String task1Json = jsonOfTask(task1);
        Task task2 = new Task("name2", " description2", START_TIME.plusMinutes(10), THIRTY_MINUTES);
        String task2Json = jsonOfTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestTask1 = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(task1Json)).build();
        HttpRequest requestTask2 = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(task2Json)).build();
        client.send(requestTask1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = client.send(requestTask2, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("name", tasksFromManager.get(0).getName());
    }

}
