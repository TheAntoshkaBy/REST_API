package com.epam.esm.exception;

import java.util.ArrayList;
import java.util.List;

public class ControllerException extends RuntimeException {
    private final static String MESSAGE = "Repository exception";
    private List<InvalidControllerOutputMessage> messages;
    private InvalidControllerOutputMessage message;

    public ControllerException(List<InvalidControllerOutputMessage> messages) {
        this.messages = messages;
    }

    public ControllerException() {
    }

    public ControllerException(InvalidControllerOutputMessage message) {
        this.message = message;
        messages = new ArrayList<>();
        messages.add(message);
    }

    public List<InvalidControllerOutputMessage> getMessages() {
        return messages;
    }

    public InvalidControllerOutputMessage getErrorMessage() {
        return message;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
