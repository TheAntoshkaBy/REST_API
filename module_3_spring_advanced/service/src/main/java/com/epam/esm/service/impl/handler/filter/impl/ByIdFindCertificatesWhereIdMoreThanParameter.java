package com.epam.esm.service.impl.handler.filter.impl;

import com.epam.esm.service.CertificateService;
import com.epam.esm.entity.CertificatePOJO;
import com.epam.esm.service.impl.handler.filter.CertificateFilterRequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class ByIdFindCertificatesWhereIdMoreThanParameter implements CertificateFilterRequestParameter {
    private static final String FILTER_TYPE = "greater than";
    private CertificateService certificateService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public List<CertificatePOJO> filterOutOurCertificates(HttpServletRequest request) {
        return certificateService.findAllCertificatesByIdThreshold(
                Long.parseLong(request.getParameterValues("id")[0])
        );
    }

    public String getType() {
        return FILTER_TYPE;
    }
}
