package main.WebServices.handlers;

import com.sun.net.httpserver.HttpExchange;
import main.WebServices.HttpTaskServer;
import main.manager.TaskManager;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseCrudHandler<Subtask> {


    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }


    protected String handleGetRequest(HttpExchange exchange) throws IOException {
        if (isThirdId(exchange)) {
            Task task = taskManager.findById(getIdFromURL(exchange));

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

    protected String handleDeleteRequest(HttpExchange httpExchange) {
        if (isThirdId(httpExchange)) {
            int id = getIdFromURL(httpExchange);
            Task task = taskManager.findById(id);
            if (task != null && task.getType().equals(TaskType.SUBTASK)) {
                super.handleDeleteRequest(httpExchange);
            }
        }
        return "deleted";
    }

}
