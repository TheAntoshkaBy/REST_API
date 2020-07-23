package com.epam.esm.service.impl.handler.filter;

import com.epam.esm.pojo.CertificatePOJO;

import java.util.List;
import java.util.Map;

public interface CertificateFilterRequestParameter {

    List<CertificatePOJO> filterOutOurCertificates(Map<String, String> request);

    String getType();
}
