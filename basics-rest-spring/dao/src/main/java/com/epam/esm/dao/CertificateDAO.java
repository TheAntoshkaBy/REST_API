package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.util.List;

public interface CertificateDAO {

    List<Certificate> findAll();

    List<Certificate> findAllByDate();

    Certificate findCertificateById(int id);

    List<Certificate> findCertificateWhereIdMoreThanParameter(int id);

    void addCertificate(Certificate certificate);

    void updateCertificate(int id, Certificate certificate);

    void deleteCertificateById(int id);

    void addTag(int id, Tag tag);

    void addTag(int idCertificate, int idTag);

    void deleteTag(int idCertificate, int idTag);

    void deleteAll();

    List<Certificate> findCertificateWhereTagNameIs(Tag tag);

    List<Certificate> findCertificateByNamePart(String text);

}
