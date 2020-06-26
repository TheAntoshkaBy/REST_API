package com.epam.esm.service.Impl.handler.filter;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.certificate.CertificateNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertificateFilterRequestParameter {

    List<Certificate> filterOutOurCertificates(HttpServletRequest request) throws CertificateNotFoundException;

    String getType();
}
