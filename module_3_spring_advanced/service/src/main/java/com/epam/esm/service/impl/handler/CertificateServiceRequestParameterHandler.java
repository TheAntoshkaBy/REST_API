package com.epam.esm.service.impl.handler;

import com.epam.esm.constant.ErrorTextMessageConstants;
import com.epam.esm.entity.InvalidDataMessage;
import com.epam.esm.exception.certificate.CertificateException;
import com.epam.esm.exception.certificate.CertificateInvalidParameterDataException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.entity.CertificatePOJO;
import com.epam.esm.service.impl.handler.filter.CertificateFilterRequestParameter;
import com.epam.esm.service.impl.handler.sort.CertificateSortBy;
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
    @Autowired
    private List<CertificateSortBy> certificateSortRequestParameterList;

    public List<CertificatePOJO> find(HttpServletRequest request){
        return filter(request);
    }

    public List<CertificatePOJO> filter(HttpServletRequest request) throws CertificateException {
        List<CertificatePOJO> result;
        if (request.getParameter("filter") == null) {
            result = sort(request);
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
                throw new CertificateInvalidParameterDataException(
                        new InvalidDataMessage(ErrorTextMessageConstants.FILTER_TYPE_NOT_EXIST)
                );
            }
        }

        return result;
    }

    public List<CertificatePOJO> sort(HttpServletRequest request) throws CertificateException {
        List<CertificatePOJO> result;
        if (request.getParameter("sort") == null) {
            result = certificateService.findAll();
        } else {
            try {
                result = certificateSortRequestParameterList.stream()
                        .filter(certificateFilter -> certificateFilter
                                .getType()
                                .equals(request.getParameter("sort")))
                        .findFirst()
                        .get()
                        .sortOurCertificates(request);
            } catch (NoSuchElementException e) {
                throw new CertificateInvalidParameterDataException(
                        new InvalidDataMessage(ErrorTextMessageConstants.SORT_TYPE_NOT_EXIST)

                );
            }
        }

        return result;
    }

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }
}
