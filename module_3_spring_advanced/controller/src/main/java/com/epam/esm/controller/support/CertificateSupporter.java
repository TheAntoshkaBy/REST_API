package com.epam.esm.controller.support;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificateList;
import com.epam.esm.pojo.CertificatePOJO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CertificateSupporter {

    public static final String ERROR_NAME = "Name must be between 2 and 70 characters!";
    public static final String ERROR_ID = "Id must be null";
    public static final String ERROR_DESCRIPTION = "Description must "
        + "be between 3 and 170 characters!";
    public static final String ERROR_NULL_DESCRIPTION = "Description must not be null!";
    public static final String ERROR_NULL_PRICE = "Price must not be null!";
    public static final String ERROR_NULL_DURATION_DAYS = "Duration days must not be null!";
    public static final String ERROR_PRICE = "Price must be positive or zero!";
    public static final String ERROR_DURATION_DAYS = "Duration days must be positive or zero!";
    public static final String ERROR_DATE_OF_CREATION = "Date of creation must be null!";
    public static final String ERROR_DATE_OF_MODIFICATION = "Date of modification must be null!";
    public static final String ERROR_TAGS_LIST = "Tags list must be null!";

    public static List<CertificateDTO> certificatePojoListToCertificateDtoList(
        List<CertificatePOJO> certificates) {
        return certificates
            .stream()
            .map(CertificateDTO::new)
            .collect(Collectors.toList());
    }

    public static CertificatePOJO certificateDtoToCertificatePOJO(CertificateDTO certificate) {
        return new CertificatePOJO(
            certificate.getName(),
            certificate.getDescription(),
            certificate.getPrice(),
            certificate.getDurationDays()
        );
    }

    public static CertificateList formationCertificateList(
        Map<List<CertificatePOJO>, Integer> certificatesMap,
        int page,
        int size,
        Map<String, String> params) {
        List<CertificatePOJO> certificates = certificatesMap
            .entrySet()
            .iterator()
            .next()
            .getKey();
        int resultCount = certificatesMap.get(certificates);
        List<CertificateDTO> certificatesDTO = CertificateSupporter
            .certificatePojoListToCertificateDtoList(certificates);

        return new CertificateList
            .CertificateListBuilder(certificatesDTO)
            .page(page)
            .size(size)
            .parameters(params)
            .resultCount(resultCount)
            .build();
    }
}
