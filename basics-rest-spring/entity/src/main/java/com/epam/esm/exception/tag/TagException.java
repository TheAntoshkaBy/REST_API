package com.epam.esm.exception.tag;

import com.epam.esm.exception.ServiceException;

public class TagException extends ServiceException {
    private static final String MESSAGE = "This tag is incorrect!";

    public TagException() {
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
