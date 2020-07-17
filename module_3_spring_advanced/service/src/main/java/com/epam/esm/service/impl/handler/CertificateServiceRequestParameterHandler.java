package com.epam.esm.service.impl.handler;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.InvalidDataMessage;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.handler.and.ComplexFilter;
import com.epam.esm.service.impl.handler.filter.CertificateFilterRequestParameter;
import com.epam.esm.service.impl.handler.sort.CertificateSortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class CertificateServiceRequestParameterHandler {

    private CertificateService certificateService;

    @Autowired
    private List<CertificateFilterRequestParameter> certificateFilterRequestParameterList;
    @Autowired
    private List<CertificateSortBy> certificateSortRequestParameterList;
    @Autowired
    private List<ComplexFilter> filters;

    public List<CertificatePOJO> find(HttpServletRequest request) {
        return filter(request);
    }

    public List<CertificatePOJO> filter(HttpServletRequest request) {
        List<CertificatePOJO> result;
        try {
            if (request.getParameter("filter") == null) {
                result = sort(request);
            } else {
                result = certificateFilterRequestParameterList.stream()
                        .filter(certificateFilter -> certificateFilter
                                .getType()
                                .equals(request.getParameter("filter")))
                        .findFirst()
                        .get()
                        .filterOutOurCertificates(request);
            }
        } catch (NoSuchElementException e) {
            throw new ServiceException(
                    new InvalidDataMessage(ErrorTextMessageConstants.FILTER_TYPE_NOT_EXIST)
            );
        }

        return result;
    }

    public List<CertificatePOJO> sort(HttpServletRequest request) {
        List<CertificatePOJO> result;
        try {
            if (request.getParameter("sort") == null) {
                result = certificateService.findAll();
            } else {
                result = certificateSortRequestParameterList.stream()
                        .filter(certificateFilter -> certificateFilter
                                .getType()
                                .equals(request.getParameter("sort")))
                        .findFirst()
                        .get()
                        .sortOurCertificates(request);
            }
        } catch (NoSuchElementException e){
            throw new ServiceException(
                    new InvalidDataMessage(ErrorTextMessageConstants.SORT_TYPE_NOT_EXIST)
            );
        }
        return result;
    }

    public Map<String, Object> filterAndSetParams(Map<String, String> parameters) {
        Map<String, Object> returnedParams = new HashMap<>();
        filters.forEach(complexFilter -> {
            String param = parameters.get(complexFilter.getType());
            if (param != null) {
                try {
                    returnedParams.put(complexFilter.getType(), complexFilter.setType(param));
                } catch (RuntimeException e) {
                    throw new ServiceException(
                            new InvalidDataMessage(complexFilter.getType(), "Invalid parameter data!")
                    );
                }

            }
        });

        return returnedParams;
    }

    private StringBuilder buildQuery(Map<String, String> parameters) {
        StringBuilder result = new StringBuilder();
        result.append(filters.get(0).getPart());
        for (int i = 1; i < filters.size(); i++) {
            String param = parameters.get(filters.get(i).getType());
            if (param != null) {
                result.append(" and ");
                result.append(filters.get(i).getPart());
            }
        }
        return result;
    }

    public String filterAnd(Map<String, String> parameters) {
        StringBuilder result;

        result = new StringBuilder("select c from certificate c where ");
        result.append(buildQuery(parameters));

        return result.toString();
    }

    public String filterAndGetCount(Map<String, String> parameters) {
        StringBuilder result;

        result = new StringBuilder("select COUNT(c) from certificate c where ");
        result.append(buildQuery(parameters));

        return result.toString();
    }

    public String filterByTagsName(List<TagPOJO> tagsPOJO) {
        StringBuilder result;

        result = new StringBuilder("select c from certificate c left join c.tags t where t.name IN ('");
        for (int i = 0; i < tagsPOJO.size() - 1; i++) {
            result.append(tagsPOJO.get(i).getName());
            result.append("', '");
        }
        result.append(tagsPOJO.get(tagsPOJO.size() - 1).getName());
        result.append("') group by c.id");

        return result.toString();
    }

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }
}
