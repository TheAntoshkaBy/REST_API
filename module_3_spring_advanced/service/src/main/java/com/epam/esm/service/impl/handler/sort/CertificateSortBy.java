package com.epam.esm.service.impl.handler.sort;

import com.epam.esm.pojo.CertificatePOJO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertificateSortBy {
    List<CertificatePOJO> sortOurCertificates(HttpServletRequest request);

    String getType();
}
