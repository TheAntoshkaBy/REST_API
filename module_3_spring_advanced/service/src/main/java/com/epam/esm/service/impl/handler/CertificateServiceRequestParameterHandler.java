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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificateServiceRequestParameterHandler {

    private CertificateService certificateService;

    @Autowired
    private List<CertificateFilterRequestParameter> certificateFilterRequestParameterList;
    @Autowired
    private List<CertificateSortBy> certificateSortRequestParameterList;
    @Autowired
    private List<ComplexFilter> filters;

    public Map<List<CertificatePOJO>, Integer> find(
        Map<String, String> request, List<TagPOJO> tags, int page, int size) {
        return filter(request, tags, page, size);
    }

    private Map<List<CertificatePOJO>, Integer> filter(
        Map<String, String> request, List<TagPOJO> tags, int page, int size) {
        List<CertificatePOJO> resultList;
        int resultCount = 0;
        Map<List<CertificatePOJO>, Integer> result = new HashMap<>();
        try {
            if (request.get("filter") == null) {
                resultList = sort(request, page, size);
                resultCount = certificateService.getCertificateCount();
            } else {
                CertificateFilterRequestParameter resultFilter =
                    certificateFilterRequestParameterList
                        .stream()
                        .filter(certificateFilter -> certificateFilter
                            .getType()
                            .equals(request.get("filter")))
                        .findFirst()
                        .get();
                resultList = resultFilter.filterOutOurCertificates(request, tags, page, size);
                resultCount = resultFilter.getCountFoundPOJO(request, tags);
            }
        } catch (NoSuchElementException e) {
            throw new ServiceException(
                new InvalidDataMessage(ErrorTextMessageConstants.FILTER_TYPE_NOT_EXIST)
            );
        }
        result.put(resultList, resultCount);
        return result;
    }

    private List<CertificatePOJO> sort(Map<String, String> request, int page, int size) {
        List<CertificatePOJO> result;
        try {
            if (request.get("sort") == null) {
                result = certificateService.findAll(page, size);
            } else {
                result = certificateSortRequestParameterList.stream()
                    .filter(certificateFilter -> certificateFilter
                        .getType()
                        .equals(request.get("sort")))
                    .findFirst()
                    .get()
                    .sortOurCertificates(request, page, size);
            }
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

    private void appender(Map<String, String> parameters, List<TagPOJO> tags,
        StringBuilder result) {
        StringBuilder buffResult = buildQuery(parameters);
        result.append(buffResult);
        if (!buffResult.toString().isEmpty()) {
            result.append(" and t.name IN ('");
        } else {
            result.append(" t.name IN ('");
        }

        for (int i = 0; i < tags.size() - 1; i++) {
            result.append(tags.get(i).getName());
            result.append("', '");
        }
        result.append(tags.get(tags.size() - 1).getName());
    }

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }
}
