package com.epam.esm.service.Impl.handler.sort.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.Impl.handler.sort.CertificateSortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class CertificateSortByDate implements CertificateSortBy {
    private static final String SORT_TYPE = "date";
    private CertificateService certificateService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public List<Certificate> sortOurCertificates(HttpServletRequest request)
            throws CertificateNotFoundException {
        return certificateService.findAllCertificatesByDate();
    }

    public String getType() {
        return SORT_TYPE;
    }
}
