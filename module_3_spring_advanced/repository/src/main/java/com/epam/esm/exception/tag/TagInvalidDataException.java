package com.epam.esm.exception.tag;

import com.epam.esm.exception.entity.InvalidDataMessage;
import com.epam.esm.exception.certificate.CertificateException;

import java.util.List;

public class TagInvalidDataException extends TagException {
    private static final String MESSAGE = "This data is invalid, please rewrite tag";
    private static String informPart;

    public TagInvalidDataException(String inform) {
        informPart = inform;
    }

    public TagInvalidDataException(InvalidDataMessage message) {
        super(message);
    }

    public TagInvalidDataException(List<InvalidDataMessage> messages) {
        super(messages);
    }

    @Override
    public String getMessage() {
        return "Error Code: " +
                CertificateException.class.getName().hashCode() + " "
                + MESSAGE + " " + informPart;
    }
}
