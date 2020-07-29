package com.epam.esm.service.impl;

import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.support.ServiceSupporter;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ShopCertificateService implements CertificateService {

    private CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler;
    private TagValidator tagValidator;
    private CertificateRepository certificateRepository;
    private TagRepository tagRepository;


    @Autowired
    @Override
    public void setCertificateRepository(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Autowired
    @Override
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    @Override
    public void setCertificateServiceRequestParameterHandler
            (CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler) {
        this.certificateServiceRequestParameterHandler = certificateServiceRequestParameterHandler;
    }

    @Autowired
    public void setTagValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    /**
     * @param params Request params for choice
     *               selection of certificate display type
     * @return Certificate list
     */
    @Override
    public List<CertificatePOJO> findAll(Map<String, String> params,  int page, int size) {
        return certificateServiceRequestParameterHandler.find(params, page, size);
    }

    public List<CertificatePOJO> findAllComplex(Map<String, String> request, List<TagPOJO> tags, int page, int size) {
        page = ServiceSupporter.setCurrentOffsetFromPageToDb(page, size);
        Map<String, Object> parametrizedRequest = certificateServiceRequestParameterHandler
                .filterAndSetParams(request);
        String query = certificateServiceRequestParameterHandler.filterAnd(request, tags);
        return ServiceSupporter.certificateEntityToCertificatePOJO(
                certificateRepository
                        .findAllComplex(query, parametrizedRequest, --page, size)
        );
    }

    public int getCountComplex(Map<String, String> request, List<TagPOJO> tags) {
        Map<String, Object> parametrizedRequest = certificateServiceRequestParameterHandler
                .filterAndSetParams(request);
        String query = certificateServiceRequestParameterHandler.filterAndGetCount(request, tags);
        return certificateRepository.findCountComplex(query, parametrizedRequest);
    }

    @Override
    public List<CertificatePOJO> findAll(int page, int size) {
        page = ServiceSupporter.setCurrentOffsetFromPageToDb(page, size);
        return ServiceSupporter.certificateEntityToCertificatePOJO(certificateRepository.findAll(--page, size)
        );
    }

    @Override
    public CertificatePOJO find(long id) {
        return new CertificatePOJO(certificateRepository.findById(id));
    }

    @Override
    public List<CertificatePOJO> findAllCertificatesByDate(int page, int size) {
        return ServiceSupporter.certificateEntityToCertificatePOJO(certificateRepository
                .findAllByDate(page, size));
    }

    @Override
    public int getCertificateCount() {
        return certificateRepository.getCertificateCount();
    }

    @Deprecated
    @Override
    public List<CertificatePOJO> findAllCertificatesByIdThreshold(long id, int page, int size) {
        return ServiceSupporter.certificateEntityToCertificatePOJO(certificateRepository.findAllByIdThreshold(id));

    }

    @Deprecated
    @Override
    public List<CertificatePOJO> findAllCertificatesByTag(TagPOJO tag, int page, int size) {
        tagValidator.isCorrectTag(tag);
        return ServiceSupporter.certificateEntityToCertificatePOJO(
                certificateRepository.findByTagName(tag.getName(), page, size)
        );
    }

    @Override
    public void delete(long id) {
        certificateRepository.delete(id);
    }

    @Override
    public void update(long id, CertificatePOJO certificate) {
        certificate.setModification(new Date());
        certificateRepository.update(certificate.pojoToEntity(), id);
    }

    @Override
    public void updatePrice(long id, double price) {
        certificateRepository.updatePrice(id, price);
    }

    @Override
    public CertificatePOJO create(CertificatePOJO certificate) {
        certificate.setCreationDate(new Date());
        return new CertificatePOJO(certificateRepository.create(certificate.pojoToEntity()));
    }

    @Override
    public void addTag(long id, TagPOJO tag) {
        tagValidator.isCorrectTag(tag);
        certificateRepository.addTag(id, tagRepository.create(tag.pojoToEntity()).getId());
    }

    @Override
    public void addTag(long idCertificate, long idTag) {
        certificateRepository.addTag(idCertificate, idTag);
    }

    @Override
    public void deleteTag(long idCertificate, long idTag) {
        certificateRepository.deleteTag(idCertificate, idTag);
    }

    @Deprecated
    @Override
    public List<CertificatePOJO> findByAllCertificatesByNamePart(String text) {
        text += '%';
        return ServiceSupporter.certificateEntityToCertificatePOJO(certificateRepository
                .findAllByNamePart(text)
        );
    }
}
