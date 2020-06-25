package com.epam.esm.service.Impl.handler;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.Impl.handler.filter.CertificateFilterRequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class CertificateServiceRequestParameterHandler {

    private CertificateService certificateService;

    @Autowired
    private List<CertificateFilterRequestParameter> certificateFilterRequestParameterList;

    public List<Certificate> filter(HttpServletRequest request) throws CertificateNotFoundException {
        List<Certificate> result;
        if (request.getParameter("filter") == null) {
            result = certificateService.findAll();
        } else {
            try {
                result = certificateFilterRequestParameterList.stream()
                        .filter(certificateFilter -> certificateFilter
                                .getType()
                                .equals(request.getParameter("filter")))
                        .findFirst()
                        .get()
                        .filterOutOurCertificates(request);
            } catch (NoSuchElementException e) {
                throw new CertificateNotFoundException();
            }
        }

        return result;
    }

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }
}
