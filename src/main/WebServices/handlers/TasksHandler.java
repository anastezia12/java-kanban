package main.WebServices.handlers;

import com.sun.net.httpserver.HttpExchange;
import main.WebServices.HttpTaskServer;
import main.manager.TaskManager;
import main.task.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class TasksHandler extends BaseCrudHandler<Task> {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected String handleGetRequest(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String[] splitStrings = requestURI.getPath().split("/");

        if (isThirdId(httpExchange)) {
            Task task = taskManager.findById(getIdFromURL(httpExchange));
            if (task == null) {
                throw new IOException("No task with this id");
            } else {
                return jsonOfTask(task);
            }
        } else {
            List<Task> tasks = taskManager.getListTasks();
            return HttpTaskServer.getGson().toJson(tasks);
        }
    }
}
