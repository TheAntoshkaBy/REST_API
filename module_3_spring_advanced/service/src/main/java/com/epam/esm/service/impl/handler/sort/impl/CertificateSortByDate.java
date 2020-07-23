package com.epam.esm.service.impl.handler.sort.impl;

import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.handler.sort.CertificateSortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CertificateSortByDate implements CertificateSortBy {
    private static final String SORT_TYPE = "date";
    private CertificateService certificateService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public List<CertificatePOJO> sortOurCertificates(Map<String, String> request) {
        return certificateService.findAllCertificatesByDate();
    }

    public String getType() {
        return SORT_TYPE;
    }
}