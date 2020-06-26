package com.epam.esm.exception.certificate;

public class CertificateNotFoundException extends CertificateException {
    private static final String MESSAGE =
            "This certificate is not available or does not exist, please check the entered data!";

    public CertificateNotFoundException() {
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
