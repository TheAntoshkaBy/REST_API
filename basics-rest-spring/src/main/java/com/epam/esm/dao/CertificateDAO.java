package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;

import java.util.ArrayList;
import java.util.List;

public interface CertificateDAO {
    List<Certificate> getAll();

    Certificate getCertificateById(Integer id);
}
