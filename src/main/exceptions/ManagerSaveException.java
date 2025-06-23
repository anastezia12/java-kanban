package main.exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, Throwable exception) {
        super(message, exception);
    }

    public ManagerSaveException(String message) {
        super(message);
    }
}
