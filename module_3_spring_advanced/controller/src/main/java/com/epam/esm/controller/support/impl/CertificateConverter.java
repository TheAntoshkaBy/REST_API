package com.epam.esm.controller.support.impl;

import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificateList;
import com.epam.esm.pojo.CertificatePOJO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CertificateConverter implements DtoConverter<CertificateDTO, CertificatePOJO> {

    @Override
    public List<CertificateDTO> convert(List<CertificatePOJO> certificate) {
        return certificate
            .stream()
            .map(CertificateDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    public CertificatePOJO convert(CertificateDTO certificate) {
        return new CertificatePOJO(certificate.getName(), certificate.getDescription(),
                                   certificate.getPrice(), certificate.getDurationDays());
    }
}
