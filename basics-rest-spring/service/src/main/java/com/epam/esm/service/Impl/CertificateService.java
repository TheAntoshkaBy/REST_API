package com.epam.esm.service.Impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.util.List;

public interface CertificateService {
    List<Certificate> findAll();

    List<Certificate> findAllCertificatesSortedByDate();

    List<Certificate> findAllCertificateWhereIdCountMoreThenParameterCount(int id);

    List<Certificate> findAllCertificatesWhichContainsParameterTag(Tag tag);

    List<Certificate> findByAllCertificatesByNamePart(String text);

    Certificate find(int id);

    void delete(int id);

    void update(int id, Certificate certificate);

    void create(Certificate certificate);

    void addTag(int id, Tag tag);

    void addTag(int idCertificate, int idTag);

    void deleteTag(int idCertificate, int idTag);
}
