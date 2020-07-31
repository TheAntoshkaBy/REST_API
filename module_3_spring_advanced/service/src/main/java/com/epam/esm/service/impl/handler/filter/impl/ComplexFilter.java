package com.epam.esm.service.impl.handler.filter.impl;

import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.handler.filter.CertificateFilterRequestParameter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComplexFilter implements CertificateFilterRequestParameter {

    private static final String FILTER_TYPE = "complex";
    private CertificateService certificateService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public List<CertificatePOJO> filterOutOurCertificates(
        Map<String, String> request,
        List<TagPOJO> tags,
        int page,
        int size) {
        return certificateService.findAllComplex(request, tags, page, size);
    }

    @Override
    public int getCountFoundPOJO(Map<String, String> request, List<TagPOJO> tags) {
        return certificateService.getCountComplex(request, tags);
    }

    @Override
    public String getType() {
        return FILTER_TYPE;
    }
}
