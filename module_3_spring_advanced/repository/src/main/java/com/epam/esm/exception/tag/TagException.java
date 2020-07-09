package com.epam.esm.exception.tag;

import com.epam.esm.exception.entity.InvalidDataMessage;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.certificate.CertificateException;

import java.util.List;

public class TagException extends ServiceException {
    private static final String MESSAGE = "This tag is incorrect!";

    public TagException(List<InvalidDataMessage> messages) {
        super(messages);
    }

    public TagException() {
    }

    public TagException(InvalidDataMessage message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Error Code: " +
                CertificateException.class.getName().hashCode() + " "
                + MESSAGE;
    }
}
