package com.epam.esm.entity;

import java.util.List;

public class CertificateList {
    private List<Certificate> certificates;

    public List<Certificate> getCertificateList() {
        return certificates;
    }

    public void setCertificateList(List<Certificate> certificateList) {
        this.certificates = certificateList;
    }

    public CertificateList(List<Certificate> certificateList) {
        this.certificates = certificateList;
    }

    public CertificateList() {
    }
}
