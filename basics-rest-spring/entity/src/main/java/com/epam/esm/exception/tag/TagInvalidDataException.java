package com.epam.esm.exception.tag;

public class TagInvalidDataException extends TagException {
    private static final String MESSAGE = "This data is invalid, please rewrite tag";
    private static String informPart;

    public TagInvalidDataException(String inform) {
        informPart = inform;
    }

    @Override
    public String getMessage() {
        return MESSAGE + " " + informPart;
    }
}
