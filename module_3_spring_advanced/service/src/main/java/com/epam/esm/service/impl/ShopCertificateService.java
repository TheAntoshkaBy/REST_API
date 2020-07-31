package com.epam.esm.service.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.constant.EntityNameConstant;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.InvalidDataMessage;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.support.ServiceSupporter;
import com.epam.esm.service.validator.TagValidator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param params Request params for choice selection of certificate display type
     * @return Certificate list
     */
    @Override
    public Map<List<CertificatePOJO>, Integer> findAll(
        Map<String, String> params, List<TagPOJO> tags, int page, int size
    ) {
        return certificateServiceRequestParameterHandler.find(params, tags, page, size);
    }

    public List<CertificatePOJO> findAllComplex(Map<String, String> request, List<TagPOJO> tags,
        int page, int size) {
        page = ServiceSupporter.convertPaginationPageToDbOffsetParameter(page, size);
        Map<String, Object> parametrizedRequest = certificateServiceRequestParameterHandler
            .filterAndSetParams(request);
        String query = certificateServiceRequestParameterHandler.filterAnd(request, tags);
        return ServiceSupporter.convertCertificateEntityToCertificatePOJO(
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
    public int findByAllCertificatesByIdThresholdCount(long id) {
        return certificateRepository.findCountAllByIdThreshold(id);
    }

    @Override
    public List<CertificatePOJO> findAll(int page, int size) {
        page = ServiceSupporter
            .convertPaginationPageToDbOffsetParameter(page, size);
        return ServiceSupporter
            .convertCertificateEntityToCertificatePOJO(certificateRepository.findAll(--page, size)
            );
    }

    @Override
    public CertificatePOJO find(long id) {
        return new CertificatePOJO(certificateRepository.findById(id));
    }

    @Override
    public List<CertificatePOJO> findAllCertificatesByDate(int page, int size) {
        return ServiceSupporter.convertCertificateEntityToCertificatePOJO(certificateRepository
            .findAllByDate(page, size));
    }

    @Override
    public int getCertificateCount() {
        return certificateRepository.getCertificateCount();
    }

    @Deprecated
    @Override
    public List<CertificatePOJO> findAllCertificatesByIdThreshold(long id, int page, int size) {
        return ServiceSupporter
            .convertCertificateEntityToCertificatePOJO(
                certificateRepository.findAllByIdThreshold(id, --page, size));
    }

    @Deprecated
    @Override
    public List<CertificatePOJO> findAllCertificatesByTag(TagPOJO tag, int page, int size) {
        tagValidator.isCorrectTag(tag);
        return ServiceSupporter.convertCertificateEntityToCertificatePOJO(
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
        certificateRepository
            .update(ServiceSupporter.convertCertificatePojoToCertificate(certificate), id);
    }

    @Override
    public void updatePrice(long id, double price) {
        certificateRepository.updatePrice(id, price);
    }

    @Override
    public CertificatePOJO create(CertificatePOJO certificate) {
        certificate.setCreationDate(new Date());
        return new CertificatePOJO(certificateRepository
            .create(ServiceSupporter.convertCertificatePojoToCertificate(certificate)));
    }

    @Override
    public void addTag(long id, TagPOJO tag) {
        tagValidator.isCorrectTag(tag);
        certificateRepository
            .addTag(id, tagRepository.create(ServiceSupporter.convertTagPojoToTag(tag)).getId());
    }

    @Override
    public void addTag(long idCertificate, long idTag) {
        certificateRepository.addTag(idCertificate, idTag);
    }

    @Override
    public void deleteTag(long idCertificate, long idTag) {
        Certificate buffCertificate = certificateRepository.findById(idCertificate);

        if (buffCertificate == null) {
            throw new ServiceException(new InvalidDataMessage(EntityNameConstant.CERTIFICATE,
                ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }

        Optional<Tag> buffTag = buffCertificate
            .getTags()
            .stream()
            .filter(tag -> tag.getId() == idTag).findFirst();
        if (buffTag.isPresent()) {
            certificateRepository.deleteTag(idCertificate, buffTag.get());
        } else {
            throw new ServiceException(new InvalidDataMessage(EntityNameConstant.CERTIFICATE,
                ErrorTextMessageConstants.NOT_FOUND_TAG));
        }
    }

    @Override
    public List<CertificatePOJO> findByAllCertificatesByNamePart(String text) {
        text += '%';
        return ServiceSupporter.convertCertificateEntityToCertificatePOJO(certificateRepository
            .findAllByNamePart(text)
        );
    }
}
