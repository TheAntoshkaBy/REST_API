package com.epam.esm.service;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.service.Impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.validator.CertificateValidator;

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
     * id more than transmitted id using DAO
     *
     * @param id Certificate Id
     * @return Certificate List
     **/
    List<Certificate> findAllCertificatesByIdThreshold(long id);

    /**
     * This method finds all certificates which
     * contain string in tag name from database using DAO
     **/
    List<Certificate> findAllCertificatesByTag(Tag tag) throws CertificateNotFoundException;

    /**
     * This method finds all certificates
     * which contain string in name from database using DAO
     **/
    List<Certificate> findByAllCertificatesByNamePart(String text) throws CertificateNotFoundException;

    /**
     * This method finds concrete Certificate By Id using DAO
     *
     * @param id Certificate Id
     * @return Certificate
     **/
    Certificate find(long id) throws CertificateNotFoundException;

    /**
     * This method delete concrete Certificate by
     * transmitted id using DAO
     *
     * @param id certificate id which will be delete
     **/
    void delete(long id) throws CertificateNotFoundException;

    /**
     * This method update concrete Certificate by transmitted id using DAO
     *
     * @param id          certificate id which will be edit
     * @param certificate edit data
     **/
    void update(long id, Certificate certificate) throws CertificateNotFoundException;

    /**
     * This method add new Certificate using DAO
     *
     * @param certificate Certificate object
     **/
    void create(Certificate certificate);

    /**
     * This method add new tag by transmitted data
     * to certificate with transmitted id using DAO
     *
     * @param id  certificate id which will be edit
     * @param tag edit data
     **/
    void addTag(long id, Tag tag);

    /**
     * This method add tag by transmitted tag id
     * to certificate with transmitted certificate id using DAO
     *
     * @param idCertificate certificate id which will be edit
     * @param idTag         tag id which we will be add to certificate
     **/
    void addTag(long idCertificate, long idTag);

    /**
     * This method delete all tags
     **/
    void deleteTag(long idCertificate, long idTag) throws CertificateNotFoundException;

    void setCertificateServiceRequestParameterHandler
            (CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler);

    void setCertificateValidator(CertificateValidator certificateValidator);
}

