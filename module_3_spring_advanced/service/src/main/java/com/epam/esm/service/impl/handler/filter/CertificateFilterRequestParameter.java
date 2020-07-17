package com.epam.esm.service.impl.handler.filter;

import com.epam.esm.pojo.CertificatePOJO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertificateFilterRequestParameter {

    List<CertificatePOJO> filterOutOurCertificates(HttpServletRequest request);

    String getType();
}
