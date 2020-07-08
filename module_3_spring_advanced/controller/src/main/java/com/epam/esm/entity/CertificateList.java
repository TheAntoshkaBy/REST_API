package com.epam.esm.entity;

import java.util.List;

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
