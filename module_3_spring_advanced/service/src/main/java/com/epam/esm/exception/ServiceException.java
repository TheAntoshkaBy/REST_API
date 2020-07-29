package com.epam.esm.exception;

import com.epam.esm.pojo.InvalidDataMessage;

import java.util.ArrayList;
import java.util.List;

public class ServiceException extends RuntimeException {
    private final static String MESSAGE = "Service exception";
    private List<InvalidDataMessage> messages;

    public ServiceException(List<InvalidDataMessage> messages) {
        this.messages = messages;
    }

    public ServiceException(InvalidDataMessage message) {
        messages = new ArrayList<>();
        messages.add(message);
    }

    public List<InvalidDataMessage> getMessages() {
        return messages;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
