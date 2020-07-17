package com.epam.esm.exception;

import com.epam.esm.exception.entity.InvalidDataOutputMessage;

import java.util.ArrayList;
import java.util.List;

public class RepositoryException extends RuntimeException {
    private final static String MESSAGE = "Repository exception";
    private List<InvalidDataOutputMessage> messages;
    private InvalidDataOutputMessage message;

    public RepositoryException(List<InvalidDataOutputMessage> messages) {
        this.messages = messages;
    }

    public RepositoryException() {
    }

    public RepositoryException(InvalidDataOutputMessage message) {
        this.message = message;
        messages = new ArrayList<>();
        messages.add(message);
    }

    public List<InvalidDataOutputMessage> getMessages() {
        return messages;
    }

    public InvalidDataOutputMessage getErrorMessage() {
        return message;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
