package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateNotFoundException;

import java.util.List;

/**
 * @author Anton Vedenichev (https://github.com/TheAntoshkaBy)
 */
public interface CertificateDAO {

    /**
     * This method finds all certificates from database
     **/
    List<Certificate> findAll() throws CertificateNotFoundException;

    /**
     * This method finds all certificates which contain string in tag name from database
     **/
    List<Certificate> findCertificateWhereTagNameIs(Tag tag) throws CertificateNotFoundException;

    /**
     * This method finds all certificates which contain string in name from database
     **/
    List<Certificate> findCertificateByNamePart(String text) throws CertificateNotFoundException;

    /**
     * This method finds all certificates from database and sorted them
     *
     * @return Certificates list
     **/
    List<Certificate> findAllByDate() throws CertificateNotFoundException;

    /**
     * This method finds concrete Certificate By Id
     *
     * @param id  Certificate Id
     * @return Certificate
     **/
    Certificate findCertificateById(int id) throws CertificateNotFoundException;

    /**
     * This method finds Certificates where Id more than @param
     *
     * @param id  Certificate Id
     * @return Certificates List
     **/
    List<Certificate> findCertificateWhereIdMoreThanParameter(int id);

    /**
     * This method add new Certificate
     *
     * @param certificate  Certificate object
     **/
    void addCertificate(Certificate certificate);

    /**
     * This method update concrete Certificate by transmitted id
     *
     * @param id  certificate id which will be edit
     * @param certificate edit data
     **/
    void updateCertificate(int id, Certificate certificate) throws CertificateNotFoundException;

    /**
     * This method delete concrete Certificate by transmitted id
     *
     * @param id  certificate id which will be delete
     **/
    void deleteCertificateById(int id) throws CertificateNotFoundException;

    /**
     * This method add tag by transmitted tag id to certificate with transmitted certificate id
     *
     * @param id  certificate id which will be edit
     * @param idTag tag id which we will be add to certificate
     **/
    void addTag(int idCertificate, int idTag);

    /**
     * This method delete tag by transmitted tag id to certificate with transmitted certificate id
     *
     * @param idCertificate  certificate id which will be edit
     * @param idTag tag id which we will be add to certificate
     **/
    void deleteTag(int idCertificate, int idTag) throws CertificateNotFoundException;

    /**
     * This method delete all tags
     **/
    void deleteAll();
}
