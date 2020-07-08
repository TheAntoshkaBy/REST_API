package com.epam.esm.service.impl.handler.sort;

import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.entity.CertificatePOJO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertificateSortBy {
    List<CertificatePOJO> sortOurCertificates(HttpServletRequest request) throws CertificateNotFoundException;

    String getType();
}
