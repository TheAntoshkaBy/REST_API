package com.epam.esm.exception.certificate;

public class CertificateInvalidDataException extends CertificateException {
    private static final String MESSAGE = "This data is invalid, please rewrite certificate";
    private static String informPart;

    public CertificateInvalidDataException(String inform) {
        informPart = inform;
    }

    @Override
    public String getMessage() {
        return MESSAGE + " " + informPart;
    }
}
