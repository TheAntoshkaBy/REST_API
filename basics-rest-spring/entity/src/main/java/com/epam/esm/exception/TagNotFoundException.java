package com.epam.esm.exception;


public class TagNotFoundException extends Exception {

    private final String MESSAGE = "This tag is not available or does not exist, please check the entered data!";

    public TagNotFoundException() {
    }

    public TagNotFoundException(String s) {
        super(s);
    }

    public TagNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TagNotFoundException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
