package com.epam.esm.service;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Anton Vedenichev (https://github.com/TheAntoshkaBy)
 */
public interface CertificateService {

    /**
     * This method delegates management to one of find methods.
     **/
    List<Certificate> findAll(HttpServletRequest params) throws CertificateNotFoundException;

    /**
     * This method finds all certificates from database using DAO
     **/
    List<Certificate> findAll() throws CertificateNotFoundException;

    /**
     * This method finds all certificates from database and sorted them using DAO
     *
     * @return Certificates list
     **/
    List<Certificate> findAllCertificatesByDate() throws CertificateNotFoundException;

    /**
     * This method finds concrete Certificate which contains
     *          id more than transmitted id using DAO
     *
     * @param id  Certificate Id
     * @return Certificate List
     **/
    List<Certificate> findAllCertificatesWhereIdMoreThenTransmittedId(int id);

    /**
     * This method finds all certificates which
     *      contain string in tag name from database using DAO
     **/
    List<Certificate> findAllCertificatesByTag(Tag tag) throws CertificateNotFoundException;

    /**
     * This method finds all certificates
     *      which contain string in name from database using DAO
     **/
    List<Certificate> findByAllCertificatesByNamePart(String text) throws CertificateNotFoundException;

    /**
     * This method finds concrete Certificate By Id using DAO
     *
     * @param id  Certificate Id
     * @return Certificate
     **/
    Certificate find(int id) throws CertificateNotFoundException;

    /**
     * This method delete concrete Certificate by
     *      transmitted id using DAO
     *
     * @param id  certificate id which will be delete
     **/
    void delete(int id) throws CertificateNotFoundException;

    /**
     * This method update concrete Certificate by transmitted id using DAO
     *
     * @param id  certificate id which will be edit
     * @param certificate edit data
     **/
    void update(int id, Certificate certificate) throws CertificateNotFoundException;

    /**
     * This method add new Certificate using DAO
     *
     * @param certificate  Certificate object
     **/
    void create(Certificate certificate);

    /**
     * This method add new tag by transmitted data
     *      to certificate with transmitted id using DAO
     *
     * @param id  certificate id which will be edit
     * @param tag edit data
     **/
    void addTag(int id, Tag tag);

    /**
     * This method add tag by transmitted tag id
     *      to certificate with transmitted certificate id using DAO
     *
     * @param idCertificate  certificate id which will be edit
     * @param idTag tag id which we will be add to certificate
     **/
    void addTag(int idCertificate, int idTag);

    /**
     * This method delete all tags
     **/
    void deleteTag(int idCertificate, int idTag) throws CertificateNotFoundException;
}
