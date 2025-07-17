package main.WebServices.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import main.WebServices.HttpTaskServer;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    public static String jsonOfTask(Task task) {
        Gson gson = HttpTaskServer.getGson();

        JsonObject jsonObject = gson.toJsonTree(task).getAsJsonObject();
        jsonObject.addProperty("type", task.getType().toString());

        return gson.toJson(jsonObject);
    }

    public void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public void sendHasInteractions(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public void successfulPost(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(201, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public Task fromJsonToTask(HttpExchange httpExchange) throws IOException {
        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        TaskType type = TaskType.valueOf(jsonObject.get("type").getAsString());

        Gson gson = HttpTaskServer.getGson();

        return switch (type) {
            case TASK -> gson.fromJson(jsonObject, Task.class);
            case EPIC -> gson.fromJson(jsonObject, Epic.class);
            case SUBTASK -> gson.fromJson(jsonObject, Subtask.class);
        };
    }


}
