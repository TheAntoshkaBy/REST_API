package com.epam.esm.service.impl.handler.filter.impl;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.InvalidDataMessage;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.handler.filter.CertificateFilterRequestParameter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ByNamePartFilter implements CertificateFilterRequestParameter {

    private static final String FILTER_TYPE = "name part";
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
        String text = request.get("searching name");
        if (text == null) {
            throw new ServiceException(
                new InvalidDataMessage(ErrorTextMessageConstants.FILTER_TYPE_NOT_EXIST)
            );
        }

        return certificateService.findByAllCertificatesByNamePart(text);
    }

    @Override
    public int getCountFoundPOJO(Map<String, String> request, List<TagPOJO> tags) {
        return certificateService.getCertificateCount();
    }

    @Override
    public String getType() {
        return FILTER_TYPE;
    }
}
