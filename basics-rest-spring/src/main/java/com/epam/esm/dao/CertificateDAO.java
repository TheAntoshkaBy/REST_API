package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateDAO {
    List<Certificate> getAll();

    Certificate getCertificateById(int id);

    void addCertificate(Certificate certificate);

    void updateCertificate(int id, Certificate certificate);

    void deleteCertificateById(int id);

}
