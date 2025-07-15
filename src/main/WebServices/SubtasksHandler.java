package main.WebServices;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case "POST":
                    successfulPost(exchange, handlePostRequest(exchange));
                    return;

                case "GET":
                    response = handleGetRequest(exchange);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange);
                    break;
                default:
                    response = "Incorrect method";
                    break;
            }
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange, e.getMessage());
            return;
        } catch (Exception e) {
            sendNotFound(exchange, e.getMessage());
            return;
        }

        sendText(exchange, response);
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException {
        Task task = super.fromJsonToTask(exchange);
        if (task.getId() == 0) {
            taskManager.addTask(task);
        } else {
            taskManager.updateTask(task);

        }
        return jsonOfTask(task);
    }

    private String handleGetRequest(HttpExchange exchange) throws IOException {
        URI requestedURI = exchange.getRequestURI();
        String[] splitURI = requestedURI.getPath().split("/");
        if (splitURI.length == 3) {
            int id = Integer.parseInt(splitURI[2]);
            Task task = taskManager.findById(id);

            if (task == null || !task.getType().equals(TaskType.SUBTASK)) {
                throw new IOException("No subtask with this id");
            } else {
                return jsonOfTask(task);
            }
        } else {
            List<Subtask> tasks = taskManager.getAllSubtasks();
            return HttpTaskServer.getGson().toJson(tasks);
        }
    }

    private String handleDeleteRequest(HttpExchange httpExchange) {
        URI requestURI = httpExchange.getRequestURI();
        String[] splitStrings = requestURI.getPath().split("/");
        int id = Integer.parseInt(splitStrings[2]);
        if (taskManager.findById(id).getType().equals(TaskType.SUBTASK)) {
            taskManager.deleteById(id);
        }
        return "deleted";
    }

}
