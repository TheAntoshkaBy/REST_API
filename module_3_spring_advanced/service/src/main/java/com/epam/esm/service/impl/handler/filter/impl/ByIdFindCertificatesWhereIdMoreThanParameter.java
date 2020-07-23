package com.epam.esm.service.impl.handler.filter.impl;

import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.handler.filter.CertificateFilterRequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ByIdFindCertificatesWhereIdMoreThanParameter implements CertificateFilterRequestParameter {
    private static final String FILTER_TYPE = "greater than";
    private CertificateService certificateService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public List<CertificatePOJO> filterOutOurCertificates(Map<String, String> request) {
        return certificateService.findAllCertificatesByIdThreshold(
                Long.parseLong(request.get("founded id"))
        );
    }

    public String getType() {
        return FILTER_TYPE;
    }
}
