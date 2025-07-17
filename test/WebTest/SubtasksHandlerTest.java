package WebTest;

import main.task.Epic;
import main.task.Status;
import main.task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static main.manager.TImeConstants.START_TIME;
import static main.manager.TImeConstants.THIRTY_MINUTES;
import static org.junit.jupiter.api.Assertions.*;


public class SubtasksHandlerTest extends BaseHttpTest {


    private final URI epicUri = URI.create("http://localhost:8080/epics");
    private final Epic epic = new Epic("epic", "description");
    private Subtask subtask;

    public SubtasksHandlerTest() throws IOException {
        super(URI.create("http://localhost:8080/subtasks"));
    }

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        String epicJson = jsonOfTask(epic);
        HttpRequest req = HttpRequest.newBuilder().uri(epicUri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        Epic createdEpic = gson.fromJson(resp.body(), Epic.class);
        epic.setId(createdEpic.getId());
        subtask = new Subtask("sub", "desc", epic, START_TIME, THIRTY_MINUTES);
    }

    @Test
    public void shouldCreateSubtask() throws IOException, InterruptedException {
        HttpResponse<String> addResp = addTask(subtask);
        assertEquals(201, addResp.statusCode());//<----- herreeee
        List<Subtask> tasksFromManager = manager.getAllSubtasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("sub", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldUpdateSubtask() throws IOException, InterruptedException {
        Subtask created = gson.fromJson(addTask(subtask).body(), Subtask.class);
        created.setStatus(Status.IN_PROGRESS);
        HttpResponse<String> updateResp = addTask(created);
        assertEquals(201, updateResp.statusCode());
        Subtask updated = manager.getAllSubtasks().get(0);
        assertEquals(Status.IN_PROGRESS, updated.getStatus());
    }

    @Test
    public void shouldDeleteSubtask() throws IOException, InterruptedException {
        Subtask created = gson.fromJson(addTask(subtask).body(), Subtask.class);
        HttpResponse<String> deleteResp = deleteTask(created.getId());
        assertEquals(200, deleteResp.statusCode());
        assertEquals("deleted", deleteResp.body());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void shouldRejectConflictingTime() throws IOException, InterruptedException {
        Subtask sub1 = new Subtask("s1", "desc", epic, START_TIME, THIRTY_MINUTES);
        Subtask sub2 = new Subtask("s2", "desc", epic, START_TIME.plusMinutes(10), THIRTY_MINUTES);
        addTask(sub1);
        HttpResponse<String> response = addTask(sub2);

        assertEquals(406, response.statusCode());
        assertEquals(1, manager.getAllSubtasks().size());
        assertFalse(response.body().contains(sub2.getName()));
    }

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        testGetMethodWhenOneTask(subtask);
    }
}
