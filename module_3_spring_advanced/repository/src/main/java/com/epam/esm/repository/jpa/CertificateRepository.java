package com.epam.esm.repository.jpa;

import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateRepository {
    void delete(long id);

    Certificate findById(long id);

    List<Certificate> findAll();

    Certificate create(Certificate certificate);

    List<Certificate> findAllByDate();

    List<Certificate> findAllByIdThreshold(long id);

    List<Certificate> findAllByNamePart(String namePart);

    void update(Certificate certificate, long id);

    void updatePrice(long id, double price);

    List<Certificate> findByTagName(String name);

    void addTag(long idCertificate, long idTag);

    void deleteTag(long idCertificate, long idTag);
}
