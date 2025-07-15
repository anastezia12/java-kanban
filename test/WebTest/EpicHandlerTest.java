package WebTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.WebServices.HttpTaskServer;
import main.manager.InMemory.InMemoryTaskManager;
import main.manager.TaskManager;
import main.task.Epic;
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

public class EpicHandlerTest {
    private TaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer server = new HttpTaskServer(manager);
    private Gson gson = HttpTaskServer.getGson();
    private HttpClient client = HttpClient.newHttpClient();
    private URI epicUri = URI.create("http://localhost:8080/epics");
    private URI subtaskUri = URI.create("http://localhost:8080/subtasks");
    private Epic epic;

    public EpicHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        server.start();
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
    public void shouldCreateEpic() throws IOException, InterruptedException {
        epic = new Epic("epic", "description");
        String epicJson = toJson(epic);

        HttpRequest request = HttpRequest.newBuilder().uri(epicUri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Epic> epics = manager.getAllEpic();
        assertEquals(1, epics.size());
        assertEquals("epic", epics.get(0).getName());
    }

    @Test
    public void shouldReturn404OnInvalidEpicId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/999"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("No epic with this id", response.body());
    }

    @Test
    public void shouldShowAllSubtasksForEpic() throws IOException, InterruptedException {
        epic = new Epic("epic", "description");
        String epicJson = toJson(epic);

        HttpRequest createEpic = HttpRequest.newBuilder().uri(epicUri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> epicResponse = client.send(createEpic, HttpResponse.BodyHandlers.ofString());
        Epic createdEpic = gson.fromJson(epicResponse.body(), Epic.class);
        epic.setId(createdEpic.getId());

        Subtask subtask = new Subtask("sub", "desc", epic, START_TIME, THIRTY_MINUTES);
        String subtaskJson = toJson(subtask);
        HttpRequest createSubtask = HttpRequest.newBuilder().uri(subtaskUri)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        client.send(createSubtask, HttpResponse.BodyHandlers.ofString());
        HttpRequest getSubs = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks"))
                .GET().build();
        HttpResponse<String> subsResponse = client.send(getSubs, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, subsResponse.statusCode());
        assertTrue(subsResponse.body().contains("sub"));
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        epic = new Epic("toDelete", "desc");
        String epicJson = toJson(epic);
        HttpRequest create = HttpRequest.newBuilder().uri(epicUri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> createResp = client.send(create, HttpResponse.BodyHandlers.ofString());
        Epic created = gson.fromJson(createResp.body(), Epic.class);

        HttpRequest delete = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + created.getId()))
                .DELETE().build();
        HttpResponse<String> deleteResp = client.send(delete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, deleteResp.statusCode());
        assertEquals("deleted", deleteResp.body());
        assertTrue(manager.getAllEpic().isEmpty());
    }

    @Test
    public void shouldRejectConflictingTimeForSubtasks() throws IOException, InterruptedException {
        epic = new Epic("epic", "desc");
        String epicJson = toJson(epic);
        HttpRequest createEpic = HttpRequest.newBuilder().uri(epicUri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(createEpic, HttpResponse.BodyHandlers.ofString());
        Epic createdEpic = gson.fromJson(response.body(), Epic.class);
        epic.setId(createdEpic.getId());

        Subtask sub1 = new Subtask("sub1", "desc", epic, START_TIME, THIRTY_MINUTES);
        Subtask sub2 = new Subtask("sub2", "desc", epic, START_TIME.plusMinutes(10), THIRTY_MINUTES);
        HttpRequest r1 = HttpRequest.newBuilder().uri(subtaskUri).POST(HttpRequest.BodyPublishers.ofString(toJson(sub1))).build();
        HttpRequest r2 = HttpRequest.newBuilder().uri(subtaskUri).POST(HttpRequest.BodyPublishers.ofString(toJson(sub2))).build();
        client.send(r1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> resp2 = client.send(r2, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, resp2.statusCode());
        assertEquals(1, manager.getAllSubtasks().size());
    }
}
