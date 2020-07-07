package com.epam.esm.exception.certificate;

import com.epam.esm.entity.InvalidDataMessage;

import java.util.List;

public class CertificateInvalidParameterDataException extends CertificateException {
    private static final String MESSAGE = "This parameter is invalid, please rewrite";
    private static final String MESSAGE_SECOND = "parameter";
    private String informPart;

    public CertificateInvalidParameterDataException(List<InvalidDataMessage> messages) {
        super(messages);
    }

    public CertificateInvalidParameterDataException() {
    }

    public CertificateInvalidParameterDataException(InvalidDataMessage message) {
        super(message);
    }

    public CertificateInvalidParameterDataException(String informPart) {
        this.informPart = informPart;
    }

    @Override
    public String getMessage() {
        return "Error Code: " +
                CertificateException.class.getName().hashCode() + " "
                + MESSAGE + " " + informPart + " " + MESSAGE_SECOND;
    }
}
