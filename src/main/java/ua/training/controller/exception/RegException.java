package ua.training.controller.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegException extends Exception {

    private boolean duplicate = false;

    public RegException(String message) {
        super(message);
    }
}
