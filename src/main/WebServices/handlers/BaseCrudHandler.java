package main.WebServices.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.task.Task;

import java.io.IOException;
import java.net.URI;

abstract class BaseCrudHandler <T> extends BaseHttpHandler implements HttpHandler {
    protected final TaskManager taskManager;

    public BaseCrudHandler(TaskManager taskManager){
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

    protected String handlePostRequest(HttpExchange exchange) throws IOException {
        Task task = super.fromJsonToTask(exchange);
        if (task.getId() == 0) {
            taskManager.addTask(task);
        } else {
            taskManager.updateTask(task);
        }
        return jsonOfTask(task);

    }

    protected abstract String handleGetRequest(HttpExchange exchange) throws IOException ;

    protected String handleDeleteRequest(HttpExchange exchange) {
        taskManager.deleteById(getIdFromURL(exchange));
        return "deleted";

    }

    protected int getIdFromURL(HttpExchange exchange){
        URI requestURI = exchange.getRequestURI();
        String[] splitStrings = requestURI.getPath().split("/");
        return  Integer.parseInt(splitStrings[2]);
    }
    protected boolean isThirdId(HttpExchange exchange){
        URI requestURI = exchange.getRequestURI();
        String[] splitURI = requestURI.getPath().split("/");
        if(splitURI.length >= 3){
            try{
                Integer.parseInt(splitURI[2]);
                return true;
            }catch (NumberFormatException e){
                return false;
            }
        }
        return false;
    }

}
