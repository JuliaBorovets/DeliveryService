package ua.training.controller.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegException extends Exception {

    private boolean duplicate = false;

    private String message;

    public RegException(Exception e) {
        super(e);
    }

    public RegException(String message) {
        super(message);
        this.message = message;
    }
}
