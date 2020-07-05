package com.epam.esm.exception.certificate;

import com.epam.esm.entity.InvalidDataMessage;
import com.epam.esm.exception.ServiceException;

import java.util.List;

public class CertificateException extends ServiceException {
    private final static String MESSAGE = "Certificate exception";

    public CertificateException() {
    }

    public CertificateException(InvalidDataMessage message) {
        super(message);
    }

    public CertificateException(List<InvalidDataMessage> messages) {
        super(messages);
    }

    @Override
    public String getMessage() {
        return "Error Code: " +
                CertificateException.class.getName().hashCode() + " "
                + MESSAGE;
    }
}
