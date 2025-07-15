package WebTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.WebServices.HttpTaskServer;
import main.manager.InMemory.InMemoryTaskManager;
import main.manager.TaskManager;
import main.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryHandlerTest {

    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer server = new HttpTaskServer(manager);
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = HttpTaskServer.getGson();
    private final URI historyUri = URI.create("http://localhost:8080/history");

    private final Task task1 = new Task("Test Task", "Description", START_TIME, THIRTY_MINUTES);

    public HistoryHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    private String jsonOfTask(Task task) {
        JsonObject json = gson.toJsonTree(task).getAsJsonObject();
        json.addProperty("type", task.getType().toString());
        return gson.toJson(json);
    }

    @Test
    public void shouldReturnEmptyHistoryInitially() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(historyUri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    public void shouldReturnHistoryAfterAccessingTask() throws IOException, InterruptedException {
        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonOfTask(task1)))
                .build();
        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        Task created = gson.fromJson(createResponse.body(), Task.class);
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + created.getId()))
                .GET()
                .build();
        client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest historyRequest = HttpRequest.newBuilder()
                .uri(historyUri)
                .GET()
                .build();
        HttpResponse<String> historyResponse = client.send(historyRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, historyResponse.statusCode());
        assertTrue(historyResponse.body().contains("Test Task"));
    }

    @Test
    public void shouldReturnErrorOnUnsupportedMethod() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(historyUri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Incorrect method", response.body());
    }
}
