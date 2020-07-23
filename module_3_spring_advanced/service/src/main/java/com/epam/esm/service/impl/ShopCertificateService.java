package com.epam.esm.service.impl;

import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShopCertificateService implements CertificateService {

    private CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler;
    private CertificateValidator certificateValidator;
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
    public void setCertificateValidator(CertificateValidator certificateValidator) {
        this.certificateValidator = certificateValidator;
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
    public List<CertificatePOJO> findAll(Map<String, String> params) {//3
        return certificateServiceRequestParameterHandler.find(params);
    }

    public List<CertificatePOJO> findAllComplex(Map<String, String> request, List<TagPOJO> tags, int page, int size) {//1
        if (page != 1) {
            page = size * (page - 1) + 1;
        }
        Map<String, Object> parametrizedRequest = certificateServiceRequestParameterHandler.filterAndSetParams(request);
        String query = certificateServiceRequestParameterHandler.filterAnd(request, tags);
        return certificateRepository.findAllComplex(query, parametrizedRequest, --page, size)
                .stream()
                .map(CertificatePOJO::new)
                .collect(Collectors.toList());
    }

    public int getCountComplex(Map<String, String> request, List<TagPOJO> tags) {//2
        Map<String, Object> parametrizedRequest = certificateServiceRequestParameterHandler.filterAndSetParams(request);
        String query = certificateServiceRequestParameterHandler.filterAndGetCount(request, tags);
        return certificateRepository.findCountComplex(query, parametrizedRequest);
    }

    @Override
    public List<CertificatePOJO> findAll(int page, int size) {
        if (page != 1) {
            page = size * (page - 1) + 1;
        }
        return certificateRepository.findAll(--page, size)
                .stream()
                .map(CertificatePOJO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CertificatePOJO find(long id) {
        return new CertificatePOJO(certificateRepository.findById(id));
    }

    @Override
    public List<CertificatePOJO> findAllCertificatesByDate() {//4
        return certificateRepository.findAllByDate()
                .stream()
                .map(CertificatePOJO::new)
                .collect(Collectors.toList());
    }

    @Override
    public int getCertificateCount() {
        return certificateRepository.getCertificateCount();
    }//5

    @Deprecated
    @Override
    public List<CertificatePOJO> findAllCertificatesByIdThreshold(long id) {
        return certificateRepository.findAllByIdThreshold(id)
                .stream()
                .map(CertificatePOJO::new)
                .collect(Collectors.toList());
    }

    @Deprecated
    @Override
    public List<CertificatePOJO> findAllCertificatesByTag(TagPOJO tag) {//7
        tagValidator.isCorrectTag(tag);
        return certificateRepository.findByTagName(tag.getName())
                .stream()
                .map(CertificatePOJO::new)
                .collect(Collectors.toList());
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
    }//9

    @Override
    public CertificatePOJO create(CertificatePOJO certificate) {//10
        certificateValidator.isCorrectCertificateCreateData(certificate);
        certificate.setCreationDate(new Date());
        return new CertificatePOJO(certificateRepository.create(certificate.pojoToEntity()));
    }

    @Override
    public void addTag(long id, TagPOJO tag) {//1
        tagValidator.isCorrectTag(tag);
        certificateRepository.addTag(id, tagRepository.create(tag.pojoToEntity()).getId());
    }

    @Override
    public void addTag(long idCertificate, long idTag) {
        certificateRepository.addTag(idCertificate, idTag);
    }//12

    @Override
    public void deleteTag(long idCertificate, long idTag) {
        certificateRepository.deleteTag(idCertificate, idTag);
    }//13

    @Deprecated
    @Override
    public List<CertificatePOJO> findByAllCertificatesByNamePart(String text) {//14
        text += '%';
        return certificateRepository.findAllByNamePart(text)
                .stream()
                .map(CertificatePOJO::new)
                .collect(Collectors.toList());
    }
}
