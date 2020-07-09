package com.epam.esm.exception.certificate;

import com.epam.esm.exception.entity.InvalidDataMessage;

import java.util.List;

public class CertificateInvalidDataException extends CertificateException {
    private static final String MESSAGE = "This parameter is invalid, please rewrite";
    private static final String MESSAGE_SECOND = "parameter";
    private static String informPart;

    public CertificateInvalidDataException(List<InvalidDataMessage> messages) {
        super(messages);
    }

    public CertificateInvalidDataException(InvalidDataMessage message) {
        super(message);
    }

    public CertificateInvalidDataException() {
    }

    @Override
    public String getMessage() {
        return "Error Code: " +
                CertificateException.class.getName().hashCode() + " "
                + MESSAGE + " " + informPart + " " + MESSAGE_SECOND;
    }
}
