package com.epam.esm.exception.certificate;

import com.epam.esm.entity.InvalidDataMessage;

import java.util.List;

public class CertificateNotFoundException extends CertificateException {
    private static final String MESSAGE =
            "This certificate is not available or does not exist, please check the entered data!";


    public CertificateNotFoundException(InvalidDataMessage message) {
        super(message);
    }

    public CertificateNotFoundException(List<InvalidDataMessage> messages) {
        super(messages);
    }

    @Override
    public String getMessage() {
        return "Error Code: " +
                CertificateException.class.getName().hashCode() + " "
                + MESSAGE;
    }
}
