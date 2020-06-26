package com.epam.esm.service.validator;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.certificate.CertificateInvalidDataException;
import org.springframework.stereotype.Component;

@Component
public class CertificateValidator {

    private boolean nameLength(String name) {
        if (name.length() > 60 || name.length() < 3) {
            throw new CertificateInvalidDataException("name");
        }
        return true;
    }

    private boolean priceCheck(double price) {
        if (price < 0) {
            throw new CertificateInvalidDataException("price");
        }
        return true;
    }

    public boolean isCorrectCertificateData(Certificate certificate) {
        return nameLength(certificate.getName()) && priceCheck(certificate.getPrice());
    }
}
