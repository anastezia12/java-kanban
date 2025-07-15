package main.WebServices;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        String method = exchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                response = handleGetRequest(exchange);
            } else {
                throw new IOException("Incorrect method");
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

    private String handleGetRequest(HttpExchange exchange) {
        return HttpTaskServer.getGson().toJson(taskManager.getPrioritizedTasks());
    }
}
