package main.WebServices.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.WebServices.HttpTaskServer;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class EpicsHandler extends BaseCrudHandler<Epic> implements HttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected String handleGetRequest(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String[] splitStrings = requestURI.getPath().split("/");

        if (isThirdId(httpExchange)) {
            Task task = taskManager.findById(getIdFromURL(httpExchange));
            if (task == null || !task.getType().equals(TaskType.EPIC)) {
                throw new IOException("No epic with this id");
            } else {
                return jsonOfTask(task);
            }
        } else if (splitStrings.length == 4) {
            if (!taskManager.findById(getIdFromURL(httpExchange)).getType().equals(TaskType.EPIC)) {
                throw new IOException("No epic with this id");
            }
            if (!splitStrings[3].equals("subtasks")) {
                throw new IOException("404 Not Found");
            }
            List<Subtask> subtasks = taskManager.getAllSubtaskFromEpic(getIdFromURL(httpExchange));
            return HttpTaskServer.getGson().toJson(subtasks);

        } else {
            List<Epic> tasks = taskManager.getAllEpic();
            return HttpTaskServer.getGson().toJson(tasks);
        }
    }

    protected String handleDeleteRequest(HttpExchange httpExchange) {
        if (isThirdId(httpExchange)) {
            int id = getIdFromURL(httpExchange);
            Task task = taskManager.findById(id);
            if (task != null && task.getType().equals(TaskType.EPIC)) {
                super.handleDeleteRequest(httpExchange);
            }
        }
        return "deleted";
    }
}
