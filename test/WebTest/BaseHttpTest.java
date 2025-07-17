package WebTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import main.WebServices.HttpTaskServer;
import main.manager.InMemory.InMemoryTaskManager;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class BaseHttpTest {
    protected final TaskManager manager = new InMemoryTaskManager();
    protected final URI url;
    protected final HttpTaskServer taskServer = new HttpTaskServer(manager);
    protected final Gson gson = HttpTaskServer.getGson();
    protected final HttpClient client = HttpClient.newHttpClient();

    public BaseHttpTest(URI url) throws IOException {
        this.url = url;
    }

    public static String jsonOfTask(Task task) {
        Gson gson = HttpTaskServer.getGson();

        JsonObject jsonObject = gson.toJsonTree(task).getAsJsonObject();
        jsonObject.addProperty("type", task.getType().toString());
        return gson.toJson(jsonObject);
    }

    public Task fromJsonToTask(HttpExchange httpExchange) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        TaskType type = TaskType.valueOf(jsonObject.get("type").getAsString());

        Gson gson = HttpTaskServer.getGson();

        return switch (type) {
            case TASK -> gson.fromJson(jsonObject, Task.class);
            case EPIC -> gson.fromJson(jsonObject, Epic.class);
            case SUBTASK -> gson.fromJson(jsonObject, Subtask.class);
        };
    }

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpic();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @ParameterizedTest
    @CsvSource({
            "/epics/999, No epic with this id",
            "/subtasks/999, No subtask with this id",
            "/tasks/999, No task with this id"
    })
    public void afterGettingWithIncorrectId(String path, String expectedBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080" + path))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals(expectedBody, response.body());
    }

    public HttpResponse<String> addTask(Task task) throws IOException, InterruptedException {
        String taskJson = jsonOfTask(task);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> deleteTask(int id) throws IOException, InterruptedException {
        HttpRequest delete = HttpRequest.newBuilder()
                .uri(URI.create(url.toString() + "/" + id))
                .DELETE().build();
        return client.send(delete, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void deletingWithIncorrectIdGivesCorrectAns() throws IOException, InterruptedException {
        HttpResponse<String> response2 = deleteTask(1);
        System.out.println(response2.body());
        assertEquals(200, response2.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager);
        assertEquals(0, tasksFromManager.size());
        assertEquals("deleted", response2.body());
    }

    public void testGetMethodWhenOneTask(Task task) throws IOException, InterruptedException {
        addTask(task);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int num = 1;
        if (task.getType().equals(TaskType.SUBTASK)) {
            num = 2;
        }
        List<Task> tasksFromManager = manager.getListTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(num, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(task.getName(), tasksFromManager.get(num - 1).getName(), "Некорректное имя задачи");
    }
}
