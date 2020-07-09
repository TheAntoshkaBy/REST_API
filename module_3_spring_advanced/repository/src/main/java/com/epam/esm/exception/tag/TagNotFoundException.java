package com.epam.esm.exception.tag;

import com.epam.esm.exception.entity.InvalidDataMessage;
import com.epam.esm.exception.certificate.CertificateException;

import java.util.List;

public class TagNotFoundException extends TagException {
    private static final String MESSAGE = "This tag is not available or does not exist, please check the entered data!";

    public TagNotFoundException() {
    }

    public TagNotFoundException(List<InvalidDataMessage> messages) {
        super(messages);
    }

    public TagNotFoundException(InvalidDataMessage message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Error Code: " +
                CertificateException.class.getName().hashCode() + " "
                + MESSAGE;
    }
}
