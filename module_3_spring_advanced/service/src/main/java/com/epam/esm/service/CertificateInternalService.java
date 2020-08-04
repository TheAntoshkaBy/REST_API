package com.epam.esm.service;

import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.TagRepository;
import com.epam.esm.service.impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.validator.TagValidator;
import java.util.List;
import java.util.Map;

/**
 * @author Anton Vedenichev (https://github.com/TheAntoshkaBy)
 */
public interface CertificateInternalService {

    /**
     * This method delegates management to one of find methods.
     **/
    List<CertificatePOJO> findAll(int page, int size);

    int getAllCertificateCount();

    /**
     * This method finds all certificates from database using DAO
     *
     * @param request Certificate request data
     * @param tags    Certificate tags
     * @param page    pagination offset
     * @param size    pagination limit
     **/
    List<CertificatePOJO> findAllComplex(Map<String, String> request, List<TagPOJO> tags, int page,
                                         int size);

    /**
     * This method finds certificates count
     *
     * @param request Certificate request data
     * @param tags    Certificate tags
     **/
    int getCountComplex(Map<String, String> request, List<TagPOJO> tags);

    /**
     * This method find certificate from database by id
     *
     * @param id Certificate id
     **/
    int findByAllCertificatesByIdThresholdCount(long id);

    /**
     * This method finds all certificates from database and sorted them using DAO
     *
     * @param page pagination offset
     * @param size pagination limit
     * @return Certificates list
     **/
    List<CertificatePOJO> findAllCertificatesByDate(int page, int size);

    /**
     * This method finds concrete Certificate which contains id more than transmitted id using DAO
     *
     * @param id   Certificate Id
     * @param page pagination offset
     * @param size pagination limit
     * @return Certificate List
     **/
    List<CertificatePOJO> findAllCertificatesByIdThreshold(long id, int page, int size);

    /**
     * This method finds all certificates which contain string in tag name from database using DAO
     *
     * @param tag  Certificate tag
     * @param page pagination offset
     * @param size pagination limit
     **/
    List<CertificatePOJO> findAllCertificatesByTag(TagPOJO tag, int page, int size);

    /**
     * This method finds all certificates which contain string in name from database using DAO
     *
     * @param text Certificate name part
     **/
    List<CertificatePOJO> findByAllCertificatesByNamePart(String text);


    void setCertificateServiceRequestParameterHandler
        (CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler);

    void setCertificateRepository(CertificateRepository certificateRepository);

    void setTagRepository(TagRepository tagRepository);

    void setTagValidator(TagValidator tagValidator);
}