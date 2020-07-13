package com.epam.esm.exception;

import com.epam.esm.exception.entity.InvalidDataMessage;

import java.util.ArrayList;
import java.util.List;

public class ServiceException extends RuntimeException { //fixme почему exception нельзя по слоям мешать
    private final static String MESSAGE = "Service exception";
    private List<InvalidDataMessage> messages;
    private InvalidDataMessage message;

    public List<InvalidDataMessage> getMessages() {
        return messages;
    }

    public ServiceException(List<InvalidDataMessage> messages) {
        this.messages = messages;
    }

    public ServiceException() {
    }

    public ServiceException(InvalidDataMessage message){
        this.message = message;
        messages = new ArrayList<>();
        messages.add(message);
    }

    public InvalidDataMessage getErrorMessage() {
        return message;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
