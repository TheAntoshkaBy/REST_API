package com.epam.esm.service.Impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CertificateService {

    List<Certificate> findAll(HttpServletRequest params);

    List<Certificate> findAll();

    List<Certificate> findAllCertificatesByDate();

    List<Certificate> findAllCertificatesWhereIdMoreThenTransmittedId(int id);

    List<Certificate> findAllCertificatesByTag(Tag tag);

    List<Certificate> findByAllCertificatesByNamePart(String text);

    Certificate find(int id);

    void delete(int id);

    void update(int id, Certificate certificate);

    void create(Certificate certificate);

    void addTag(int id, Tag tag);

    void addTag(int idCertificate, int idTag);

    void deleteTag(int idCertificate, int idTag);

}
