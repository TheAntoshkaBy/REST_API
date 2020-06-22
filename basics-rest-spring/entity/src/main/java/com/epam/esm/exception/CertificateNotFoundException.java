package com.epam.esm.exception;

public class CertificateNotFoundException extends Exception {
    private final String MESSAGE = "This certificate is not available or does not exist, please check the entered data!";

    public CertificateNotFoundException() {
    }

    public CertificateNotFoundException(String s) {
        super(s);
    }

    public CertificateNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CertificateNotFoundException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
