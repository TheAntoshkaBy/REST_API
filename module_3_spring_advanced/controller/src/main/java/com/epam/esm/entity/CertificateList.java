package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateList {
    private List<CertificateDTO> certificates;

    public List<CertificateDTO> getCertificateList() {
        return certificates;
    }

    public void setCertificateList(List<CertificateDTO> certificateList) {
        this.certificates = certificateList;
    }

    public CertificateList(List<CertificateDTO> certificateList) {
        this.certificates = certificateList;
    }

    public CertificateList() {
    }
}
