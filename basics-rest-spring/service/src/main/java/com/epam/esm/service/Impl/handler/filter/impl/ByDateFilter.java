package com.epam.esm.service.Impl.handler.filter.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.Impl.handler.filter.CertificateFilterRequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class ByDateFilter implements CertificateFilterRequestParameter {
    private static final String FILTER_TYPE = "date";
    private CertificateService certificateService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public List<Certificate> filterOutOurCertificates(HttpServletRequest request)
            throws CertificateNotFoundException {
        return certificateService.findAllCertificatesByDate();
    }

    public String getType() {
        return FILTER_TYPE;
    }
}
