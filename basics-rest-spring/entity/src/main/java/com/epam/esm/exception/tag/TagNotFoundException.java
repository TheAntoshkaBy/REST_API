package com.epam.esm.exception.tag;

public class TagNotFoundException extends TagException {
    private static final String MESSAGE = "This tag is not available or does not exist, please check the entered data!";

    public TagNotFoundException() {
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
