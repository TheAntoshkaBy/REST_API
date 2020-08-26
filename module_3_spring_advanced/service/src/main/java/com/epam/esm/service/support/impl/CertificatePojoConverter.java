package com.epam.esm.service.support.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.service.support.PojoConverter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CertificatePojoConverter implements PojoConverter<CertificatePOJO, Certificate> {

    @Override
    public List<CertificatePOJO> convert(List<Certificate> certificates) {
        return certificates.stream()
            .map(CertificatePOJO::new)
            .collect(Collectors.toList());
    }

    @Override
    public Certificate convert(CertificatePOJO certificate) {
        if (certificate.getId() == null) {
            return new Certificate(certificate.getName(), certificate.getDescription(),
                                   certificate.getPrice(), certificate.getDurationDays()
            );
        } else {
            return new Certificate(certificate.getId(), certificate.getName(),
                                   certificate.getDescription(), certificate.getPrice(),
                                   certificate.getDurationDays()
            );
        }
    }
}
