package com.epam.esm.exception.certificate;

import com.epam.esm.exception.ServiceException;

public class CertificateException extends ServiceException {
    private final static String MESSAGE = "Certificate exception";

    public CertificateException() {
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
