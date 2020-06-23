package com.epam.esm.service.Impl.handler.filter.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.Impl.handler.filter.CertificateFilterRequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class ByNamePartFilter implements CertificateFilterRequestParameter {

    private final String FILTER_TYPE = "by name part";
    private CertificateService certificateService;

    @Override
    public List<Certificate> filterOutOurCertificates(HttpServletRequest request)
            throws CertificateNotFoundException {
        return certificateService
                .findByAllCertificatesByNamePart(request.getParameterValues("name")[0]);
    }

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public String getType() {
        return FILTER_TYPE;
    }
}
