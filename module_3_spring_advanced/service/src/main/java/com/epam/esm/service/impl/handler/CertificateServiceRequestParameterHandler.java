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

    public List<CertificatePOJO> find(Map<String, String> request) {
        return filter(request);
    }

    public List<CertificatePOJO> filter(Map<String, String> request) {
        List<CertificatePOJO> result;
        try {
            if (request.get("filter") == null) {
                result = sort(request);
            } else {
                result = certificateFilterRequestParameterList.stream()
                        .filter(certificateFilter -> certificateFilter
                                .getType()
                                .equals(request.get("filter")))
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

    public List<CertificatePOJO> sort(Map<String, String> request) {
        List<CertificatePOJO> result;
        try {
            result = certificateSortRequestParameterList.stream()
                    .filter(certificateFilter -> certificateFilter
                            .getType()
                            .equals(request.get("sort")))
                    .findFirst()
                    .get()
                    .sortOurCertificates(request);
        } catch (NoSuchElementException e) {
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
        boolean isFirst = true;
        for (ComplexFilter filter : filters) {
            String param = parameters.get(filter.getType());
            if (param != null) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    result.append(" and ");
                }
                result.append(filter.getPart());
            }
        }
        return result;
    }

    public String filterAnd(Map<String, String> parameters, List<TagPOJO> tags) {
        StringBuilder result;

        if (tags == null) {
            result = new StringBuilder("select c from certificate c where ");
            StringBuilder buffResult = buildQuery(parameters);
            if (buffResult.toString().isEmpty()) {
                throw new ServiceException(new InvalidDataMessage("Equals search parameters!"));
            }
            result.append(buffResult);
        } else {
            result = new
                    StringBuilder("select c from certificate c " +
                    "join c.tags t where ");
            appender(parameters, tags, result);
            result.append("') group by c.id");
        }

        return result.toString();
    }

    public String filterAndGetCount(Map<String, String> parameters, List<TagPOJO> tags) {
        StringBuilder result;

        if (tags == null) {
            result = new StringBuilder("select COUNT(c) from certificate c where ");
            StringBuilder buffResult = buildQuery(parameters);
            if (buffResult.toString().isEmpty()) {
                throw new ServiceException(new InvalidDataMessage("Equals search parameters!"));
            }
            result.append(buffResult);
        } else {
            result = new
                    StringBuilder("select COUNT(c) from certificate c " +
                    "join c.tags t where ");
            appender(parameters, tags, result);
            result.append("')");

        }
        return result.toString();
    }

    private void appender(Map<String, String> parameters, List<TagPOJO> tags, StringBuilder result) {
        StringBuilder buffResult = buildQuery(parameters);
        result.append(buffResult);
        if (!buffResult.toString().isEmpty())
            result.append(" and t.name IN ('");
        else
            result.append(" t.name IN ('");

        for (int i = 0; i < tags.size() - 1; i++) {
            result.append(tags.get(i).getName());
            result.append("', '");
        }
        result.append(tags.get(tags.size() - 1).getName());
    }

    /*public String filterByTagsName(List<TagPOJO> tagsPOJO) {
        StringBuilder result;

        result = new StringBuilder("select c from certificate c left join c.tags t where t.name IN ('");
        for (int i = 0; i < tagsPOJO.size() - 1; i++) {
            result.append(tagsPOJO.get(i).getName());
            result.append("', '");
        }
        result.append(tagsPOJO.get(tagsPOJO.size() - 1).getName());
        result.append("') group by c.id");

        return result.toString();
    }*/

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }
}
