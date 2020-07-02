package com.epam.esm.service.Impl.handler.sort;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.certificate.CertificateNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertificateSortBy {
    List<Certificate> sortOurCertificates(HttpServletRequest request) throws CertificateNotFoundException;

    String getType();
}
