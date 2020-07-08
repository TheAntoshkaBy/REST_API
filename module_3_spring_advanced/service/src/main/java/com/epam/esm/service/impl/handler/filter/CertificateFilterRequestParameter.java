package com.epam.esm.service.impl.handler.filter;

import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.entity.CertificatePOJO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertificateFilterRequestParameter {

    List<CertificatePOJO> filterOutOurCertificates(HttpServletRequest request) throws CertificateNotFoundException;

    String getType();
}
