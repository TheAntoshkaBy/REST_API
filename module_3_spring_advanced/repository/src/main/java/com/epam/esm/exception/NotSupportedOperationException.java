package com.epam.esm.exception;

import com.epam.esm.exception.entity.InvalidDataOutputMessage;

import java.util.List;

public class NotSupportedOperationException extends RepositoryException{
    public NotSupportedOperationException(List<InvalidDataOutputMessage> messages) {
        super(messages);
    }

    public NotSupportedOperationException() {
        super();
    }

    public NotSupportedOperationException(InvalidDataOutputMessage message) {
        super(message);
    }

    @Override
    public List<InvalidDataOutputMessage> getMessages() {
        return super.getMessages();
    }

    @Override
    public InvalidDataOutputMessage getErrorMessage() {
        return super.getErrorMessage();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
