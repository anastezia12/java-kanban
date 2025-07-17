package main.WebServices.handlers;

import com.sun.net.httpserver.HttpExchange;
import main.WebServices.HttpTaskServer;
import main.manager.TaskManager;

public class PrioritizedHandler extends OnlyGetHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    protected String handleGetRequest(HttpExchange exchange) {
        return HttpTaskServer.getGson().toJson(taskManager.getPrioritizedTasks());
    }
}
