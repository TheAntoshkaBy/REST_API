package com.epam.esm.exception;

public class ServiceException extends RuntimeException {
    private final static String MESSAGE = "Service exception";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
