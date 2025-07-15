package main.WebServices;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.task.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
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
        List<Task> history = taskManager.getHistoryManager().getHistory();

        return HttpTaskServer.getGson().toJson(history);
    }


}
