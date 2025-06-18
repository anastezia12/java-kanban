package main.exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, Throwable ex) {
        super(message, ex);
    }

    public ManagerSaveException(String message) {
        super(message);
    }
}
