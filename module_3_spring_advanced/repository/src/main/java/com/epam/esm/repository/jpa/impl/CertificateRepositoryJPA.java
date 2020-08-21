package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryNotFoundException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.exception.entity.InvalidDataOutputMessage;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.ShopJPARepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

@Repository
public class CertificateRepositoryJPA extends ShopJPARepository<Certificate> implements
    CertificateRepository {

    @Override
    public void delete(long id) {
        int col = entityManager.createQuery(SQLRequests.DELETE_CERTIFICATE)
            .setParameter(1, id).executeUpdate();

        if (col == 0) {
            throw new RepositoryNotFoundException(
                new InvalidDataOutputMessage(ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }
    }

    @Override
    public Certificate findById(long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        if (certificate == null) {
            throw new RepositoryNotFoundException(new
                InvalidDataOutputMessage(ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }

        return certificate;
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    @Override
    public List<Certificate> findAll() {
        return (List<Certificate>) entityManager.createQuery(SQLRequests.FIND_ALL_CERTIFICATES);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Certificate> findAll(int offset, int limit) {
        return (List<Certificate>) entityManager
            .createQuery(SQLRequests.FIND_ALL_CERTIFICATES_WITH_LIMIT_OFFSET)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Certificate> findAllComplex(String query, Map<String, Object> params, int offset,
                                            int limit) {
        Query finalQuery = entityManager.createQuery(query);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            finalQuery.setParameter(entry.getKey(), entry.getValue());
        }

        return finalQuery.setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    @Override
    public int findCountComplex(String query, Map<String, Object> params) {
        Query finalQuery = entityManager.createQuery(query);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            finalQuery.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = (Long) finalQuery.getSingleResult();

        return count.intValue();
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public List<Certificate> findAllByDate(int page, int size) {

        return (List<Certificate>) entityManager
            .createQuery(SQLRequests.FIND_ALL_CERTIFICATES_BY_DATE)
            .setFirstResult(page)
            .setMaxResults(size)
            .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Certificate> findAllByIdThreshold(long id, int page, int size) {
        return entityManager
            .createQuery(SQLRequests.FIND_ALL_CERTIFICATES_WHERE_ID_MORE_THAN_PARAMETER)
            .setParameter(1, id)
            .setFirstResult(page)
            .setMaxResults(size)
            .getResultList();
    }

    public int findCountAllByIdThreshold(long id) {
        Long count = (Long) entityManager
            .createQuery(SQLRequests.FIND_COUNT_ALL_CERTIFICATES_WHERE_ID_MORE_THAN_PARAMETER)
            .setParameter(1, id)
            .getSingleResult();

        return count.intValue();
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public List<Certificate> findAllByNamePart(String namePart) {
        String nameProcedure = "findByNameProcedure";
        String procedureParam = "text";

        StoredProcedureQuery findByName = entityManager
            .createNamedStoredProcedureQuery(nameProcedure).setParameter(procedureParam, namePart);

        return findByName.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Certificate> findAllByTags(String query, int offset, int limit) {
        return entityManager
            .createQuery(query)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    public void update(Certificate certificate, Certificate updatedData) {
        certificate.setName(updatedData.getName());
        certificate.setDescription(updatedData.getDescription());
        certificate.setDurationDays(updatedData.getDurationDays());
        certificate.setPrice(updatedData.getPrice());
    }

    @SuppressWarnings("unchecked")
    public List<Certificate> findByTagName(String name, int offset, int limit) {
        return entityManager.createQuery(SQLRequests.FIND_CERTIFICATE_BY_TAG_NAME)
            .setParameter(1, name)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    public void addTag(long idCertificate, long idTag) {
        try {
            Certificate buffCertificate = entityManager.find(Certificate.class, idCertificate);
            Tag buffTag = entityManager.find(Tag.class, idTag);
            buffCertificate.getTags().add(buffTag);
        }catch (NullPointerException e){
            throw new RepositoryNotFoundException(
                new InvalidDataOutputMessage(ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }
    }

    public void addTag(Certificate certificate, Tag tag) {
        certificate.getTags().add(tag);
    }

    public void deleteTag(long idCertificate, Tag buffTag) {
        Certificate buffCertificate = entityManager.find(Certificate.class, idCertificate);
        buffCertificate.getTags().remove(buffTag);
    }

    @Override
    public int getCertificateCount() {
        Long count = (Long) entityManager.createQuery(SQLRequests.FIND_COUNT_OF_CERTIFICATE)
            .getSingleResult();

        return count.intValue();
    }
}
