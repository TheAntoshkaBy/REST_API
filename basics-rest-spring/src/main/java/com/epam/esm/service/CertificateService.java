package com.epam.esm.service;

import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateService {
    List<Certificate> getAll();

    Certificate get(Integer id);
}
