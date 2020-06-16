package com.epam.esm.service;

import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateService {
    List<Certificate> findAll();

    Certificate find(int id);

    void delete(int id);

    void update(int id, Certificate certificate);

    void create(Certificate certificate);
}
