package com.epam.esm.exception;

import lombok.Data;

@Data
public class InvalidControllerOutputMessage {
    private String parameter;
    private String message;

    public InvalidControllerOutputMessage(String parameter, String message) {
        this.message = message;
        this.parameter = parameter;
    }
}
