package ua.training.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegException extends Exception {

    private boolean duplicate = false;

    public RegException(Exception e) {
        super(e);
    }

}
