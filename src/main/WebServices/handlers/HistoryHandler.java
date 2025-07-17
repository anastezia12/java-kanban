package main.WebServices.handlers;

import com.sun.net.httpserver.HttpExchange;
import main.WebServices.HttpTaskServer;
import main.manager.TaskManager;

public class HistoryHandler extends OnlyGetHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }


    protected String handleGetRequest(HttpExchange exchange) {
        return HttpTaskServer.getGson().toJson(taskManager.getHistoryManager().getHistory());
    }


}
