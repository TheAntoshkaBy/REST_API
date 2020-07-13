package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.exception.entity.InvalidDataMessage;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.ShopJPARepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class CertificateRepositoryJPA extends ShopJPARepository<Certificate> implements CertificateRepository { //fixme попробовать с проперти SQLRequest

    @Override
    public void delete(long id) {
        if (entityManager.createQuery(SQLRequests.DELETE_CERTIFICATE)
                .setParameter(1, id).executeUpdate() == 0) {
            throw new CertificateNotFoundException(new InvalidDataMessage(
                    ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE
            ));
        }
    }

    @Override
    public Certificate findById(long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);

        if (certificate == null) {
            throw new CertificateNotFoundException(new InvalidDataMessage
                    (ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }

        return certificate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Certificate> findAll() {
        List<Certificate> certificates = entityManager
                .createQuery(SQLRequests.FIND_ALL_CERTIFICATES)
                .getResultList();

        if (certificates == null) {
            throw new CertificateNotFoundException(new InvalidDataMessage
                    (ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }

        return certificates;
    }

    @SuppressWarnings("unchecked")
    public List<Certificate> findAllByDate() {
        List<Certificate> certificates = entityManager.createQuery(SQLRequests.FIND_ALL_CERTIFICATES_BY_DATE).getResultList();

        if (certificates == null) {
            throw new CertificateNotFoundException(new InvalidDataMessage
                    (ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }

        return certificates;
    }

    @SuppressWarnings("unchecked")
    public List<Certificate> findAllByIdThreshold(long id) {
        return entityManager.createQuery(SQLRequests.FIND_ALL_CERTIFICATES_WHERE_ID_MORE_THAN_PARAMETER)
                .setParameter(1, id)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Certificate> findAllByNamePart(String namePart) {
        StoredProcedureQuery findByName = entityManager
                .createNamedStoredProcedureQuery("findByNameProcedure").setParameter("text", namePart);
        return findByName.getResultList();
    }

    public void update(Certificate certificate, long id) { //fixme состояния entity  manger почитать!
        Certificate buffCert = entityManager.find(Certificate.class, id);

        buffCert.setName(certificate.getName());
        buffCert.setDescription(certificate.getDescription());
        buffCert.setDurationDays(certificate.getDurationDays());
        buffCert.setPrice(certificate.getPrice());
    }

    @Override
    public void updatePrice(long id, double price) {
        Certificate buffCert = entityManager.find(Certificate.class, id);
        buffCert.setPrice(price);
    }

    @SuppressWarnings("unchecked")
    public List<Certificate> findByTagName(String name) {
        return entityManager.createQuery(SQLRequests.FIND_CERTIFICATE_BY_TAG_NAME)
                .setParameter(1, name).getResultList();
    }

    public void addTag(long idCertificate, long idTag) {
        Certificate buffCertificate = entityManager.find(Certificate.class, idCertificate);
        Tag buffTag = entityManager.find(Tag.class, idTag);
        buffCertificate.getTags().add(buffTag);
    }

    public void deleteTag(long idCertificate, long idTag) {
        Certificate buffCertificate = entityManager.find(Certificate.class, idCertificate);
        Optional<Tag> buffTag = buffCertificate.getTags().stream().filter(tag -> tag.getId() == idTag).findFirst();
        buffTag.ifPresent(tag -> buffCertificate.getTags().remove(tag));
    }
}
