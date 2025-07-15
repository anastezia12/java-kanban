package WebTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.WebServices.HttpTaskServer;
import main.manager.InMemory.InMemoryTaskManager;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SubtasksHandlerTest {

    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer server = new HttpTaskServer(manager);
    private final Gson gson = HttpTaskServer.getGson();
    private final HttpClient client = HttpClient.newHttpClient();
    private final URI subtaskUri = URI.create("http://localhost:8080/subtasks");
    private final URI epicUri = URI.create("http://localhost:8080/epics");
    private final Epic epic = new Epic("epic", "description");

    public SubtasksHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        server.start();
        String epicJson = toJson(epic);
        HttpRequest req = HttpRequest.newBuilder().uri(epicUri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        Epic createdEpic = gson.fromJson(resp.body(), Epic.class);
        epic.setId(createdEpic.getId());
    }

    @AfterEach
    public void shutdown() {
        server.stop();
    }

    private String toJson(Task task) {
        JsonObject json = gson.toJsonTree(task).getAsJsonObject();
        json.addProperty("type", task.getType().toString());
        return gson.toJson(json);
    }

    @Test
    public void shouldCreateSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("sub", "desc", epic, START_TIME, THIRTY_MINUTES);
        String json = toJson(subtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(subtaskUri)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(1, subtasks.size());
        assertEquals("sub", subtasks.get(0).getName());
    }

    @Test
    public void shouldReturn404OnInvalidSubtaskId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/999"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("No subtask with this id", response.body());
    }

    @Test
    public void shouldUpdateSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("original", "desc", epic, START_TIME, THIRTY_MINUTES);
        String json = toJson(subtask);
        HttpRequest createReq = HttpRequest.newBuilder().uri(subtaskUri).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> createResp = client.send(createReq, HttpResponse.BodyHandlers.ofString());
        Subtask created = gson.fromJson(createResp.body(), Subtask.class);
        created.setStatus(Status.IN_PROGRESS);

        String updateJson = toJson(created);
        HttpRequest updateReq = HttpRequest.newBuilder().uri(subtaskUri).POST(HttpRequest.BodyPublishers.ofString(updateJson)).build();
        HttpResponse<String> updateResp = client.send(updateReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, updateResp.statusCode());
        Subtask updated = manager.getAllSubtasks().get(0);
        assertEquals(Status.IN_PROGRESS, updated.getStatus());
    }

    @Test
    public void shouldDeleteSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("toDelete", "desc", epic, START_TIME, THIRTY_MINUTES);
        String json = toJson(subtask);
        HttpRequest postReq = HttpRequest.newBuilder().uri(subtaskUri).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> postResp = client.send(postReq, HttpResponse.BodyHandlers.ofString());
        Subtask created = gson.fromJson(postResp.body(), Subtask.class);
        HttpRequest deleteReq = HttpRequest.newBuilder().uri(URI.create(subtaskUri + "/" + created.getId())).DELETE().build();
        HttpResponse<String> deleteResp = client.send(deleteReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteResp.statusCode());
        assertEquals("deleted", deleteResp.body());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void shouldRejectConflictingTime() throws IOException, InterruptedException {
        Subtask sub1 = new Subtask("s1", "desc", epic, START_TIME, THIRTY_MINUTES);
        Subtask sub2 = new Subtask("s2", "desc", epic, START_TIME.plusMinutes(10), THIRTY_MINUTES);
        HttpRequest r1 = HttpRequest.newBuilder().uri(subtaskUri).POST(HttpRequest.BodyPublishers.ofString(toJson(sub1))).build();
        HttpRequest r2 = HttpRequest.newBuilder().uri(subtaskUri).POST(HttpRequest.BodyPublishers.ofString(toJson(sub2))).build();
        client.send(r1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> resp2 = client.send(r2, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, resp2.statusCode());
        assertEquals(1, manager.getAllSubtasks().size());
    }
}
