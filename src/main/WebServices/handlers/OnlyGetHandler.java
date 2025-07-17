package main.WebServices.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;

import java.io.IOException;

abstract class OnlyGetHandler extends BaseHttpHandler implements HttpHandler {
    protected TaskManager taskManager;

    public OnlyGetHandler(TaskManager taskManager) {
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

    protected abstract String handleGetRequest(HttpExchange exchange);

}
