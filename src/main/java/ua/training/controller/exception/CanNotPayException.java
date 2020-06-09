package ua.training.controller.exception;

public class CanNotPayException extends Exception {

    public CanNotPayException(String message) {
        super(message);
    }
}
