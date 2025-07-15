package WebTest;

import com.google.gson.Gson;
import main.WebServices.HttpTaskServer;
import main.manager.InMemory.InMemoryTaskManager;
import main.manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedHandlerTest {

    private final TaskManager manager = new InMemoryTaskManager();
    private final HttpTaskServer server = new HttpTaskServer(manager);
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = HttpTaskServer.getGson();
    ;
    private final URI prioritizedUri = URI.create("http://localhost:8080/prioritized");

    public PrioritizedHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    public void shouldReturnEmptyPrioritizedListInitially() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(prioritizedUri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldRejectUnsupportedMethod() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(prioritizedUri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("Incorrect method", response.body());
    }
}
