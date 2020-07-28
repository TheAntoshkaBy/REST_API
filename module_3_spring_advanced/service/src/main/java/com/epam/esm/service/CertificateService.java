package com.epam.esm.service;

import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.TagRepository;
import com.epam.esm.service.impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.TagValidator;

import java.util.List;
import java.util.Map;

/**
 * @author Anton Vedenichev (https://github.com/TheAntoshkaBy)
 */
public interface CertificateService {

    /**
     * This method delegates management to one of find methods.
     **/
    List<CertificatePOJO> findAll(Map<String, String> params, int page, int size);

    int getCertificateCount();

    List<CertificatePOJO> findAll(int page, int size);

    /**
     * This method finds all certificates from database using DAO
     **/
    List<CertificatePOJO> findAllComplex(Map<String, String> request, List<TagPOJO> tags, int page, int size);

    int getCountComplex(Map<String, String> request, List<TagPOJO> tags);

    //List<CertificatePOJO> findAllByTags(List<TagPOJO> tagsPOJO, int page, int size);


    /**
     * This method finds all certificates from database and sorted them using DAO
     *
     * @return Certificates list
     **/
    List<CertificatePOJO> findAllCertificatesByDate(int page, int size);

    /**
     * This method finds concrete Certificate which contains
     * id more than transmitted id using DAO
     *
     * @param id Certificate Id
     * @return Certificate List
     **/
    List<CertificatePOJO> findAllCertificatesByIdThreshold(long id, int page, int size);

    /**
     * This method finds all certificates which
     * contain string in tag name from database using DAO
     **/
    List<CertificatePOJO> findAllCertificatesByTag(TagPOJO tag,  int page, int size);

    /**
     * This method finds all certificates
     * which contain string in name from database using DAO
     **/
    List<CertificatePOJO> findByAllCertificatesByNamePart(String text);

    /**
     * This method finds concrete Certificate By Id using DAO
     *
     * @param id Certificate Id
     * @return Certificate
     **/
    CertificatePOJO find(long id);

    /**
     * This method delete concrete Certificate by
     * transmitted id using DAO
     *
     * @param id certificate id which will be delete
     **/
    void delete(long id);

    /**
     * This method update concrete Certificate by transmitted id using DAO
     *
     * @param id          certificate id which will be edit
     * @param certificate edit data
     **/
    void update(long id, CertificatePOJO certificate);

    /**
     * This method update concrete Certificate field (price) by transmitted id and new price value
     *
     * @param id    certificate id which will be edit
     * @param price edit data
     **/
    void updatePrice(long id, double price);

    /**
     * This method add new Certificate using DAO
     *
     * @param certificate Certificate object
     **/
    CertificatePOJO create(CertificatePOJO certificate);

    /**
     * This method add new tag by transmitted data
     * to certificate with transmitted id using DAO
     *
     * @param id  certificate id which will be edit
     * @param tag edit data
     **/
    void addTag(long id, TagPOJO tag);

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
    void deleteTag(long idCertificate, long idTag);

    void setCertificateServiceRequestParameterHandler
            (CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler);

    void setCertificateValidator(CertificateValidator certificateValidator);

    void setCertificateRepository(CertificateRepository certificateRepository);

    void setTagRepository(TagRepository tagRepository);

    void setTagValidator(TagValidator tagValidator);
}

