package WebTest;

import main.task.Epic;
import main.task.Subtask;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;
import static org.junit.jupiter.api.Assertions.*;

public class EpicHandlerTest extends BaseHttpTest {
    private Epic epic = new Epic("name", "description");

    public EpicHandlerTest() throws IOException {
        super(URI.create("http://localhost:8080/epics"));
    }

    @Test
    public void shouldCreateEpic() throws IOException, InterruptedException {

        HttpResponse<String> addResp = addTask(epic);
        assertEquals(201, addResp.statusCode());
        List<Epic> tasksFromManager = manager.getAllEpic();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("name", tasksFromManager.get(0).getName(), "Некорректное имя задачи");

    }

    @Test
    public void shouldShowAllSubtasksForEpic() throws IOException, InterruptedException {
        Epic createdEpic = gson.fromJson(addTask(epic).body(), Epic.class);
        epic.setId(createdEpic.getId());


        Subtask subtask = new Subtask("sub", "desc", epic, START_TIME, THIRTY_MINUTES);

        addTask(subtask);
        HttpRequest getSubs = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks"))
                .GET().build();
        HttpResponse<String> subsResponse = client.send(getSubs, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, subsResponse.statusCode());
        assertTrue(subsResponse.body().contains("sub"));
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        Epic created = gson.fromJson(addTask(epic).body(), Epic.class);
        HttpResponse<String> deleteResp = deleteTask(created.getId());

        assertEquals(200, deleteResp.statusCode());
        assertEquals("deleted", deleteResp.body());
        assertTrue(manager.getAllEpic().isEmpty());
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        testGetMethodWhenOneTask(epic);
    }
}

