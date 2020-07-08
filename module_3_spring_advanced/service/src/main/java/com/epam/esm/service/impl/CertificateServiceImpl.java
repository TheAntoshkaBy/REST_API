package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagPOJO;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.repository.jpa.impl.CertificateJPQLJPARepository;
import com.epam.esm.repository.jpa.impl.TagJPAJPQLRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.entity.CertificatePOJO;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler;
    private CertificateValidator certificateValidator;
    private TagValidator tagValidator;
    private CertificateJPQLJPARepository certificateRepository;
    private TagJPAJPQLRepository tagRepository;


    @Autowired
    public void setCertificateRepository(CertificateJPQLJPARepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Autowired
    public void setCertificateValidator(CertificateValidator certificateValidator) {
        this.certificateValidator = certificateValidator;
    }

    @Autowired
    public void setTagRepository(TagJPAJPQLRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
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
     * @throws CertificateNotFoundException
     */
    @Override
    public List<CertificatePOJO> findAll(HttpServletRequest params) {
        return certificateServiceRequestParameterHandler.find(params);
    }

    @Override
    public List<CertificatePOJO> findAll() {
        return certificateRepository.findAll()
                .stream()
                .map(CertificatePOJO::entityToPOJO)
                .collect(Collectors.toList());
    }

    @Override
    public CertificatePOJO find(long id) {
        return CertificatePOJO.entityToPOJO(certificateRepository.findById(id));
    }

    @Override
    public List<CertificatePOJO> findAllCertificatesByDate() {
        return certificateRepository.findAllByDate()
                .stream()
                .map(CertificatePOJO::entityToPOJO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificatePOJO> findAllCertificatesByIdThreshold(long id) {
        return certificateRepository.findAllByIdThreshold(id).stream()
                .map(CertificatePOJO::entityToPOJO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificatePOJO> findAllCertificatesByTag(TagPOJO tag) {
        return certificateRepository.findByTagName(tag.getName())
                .stream()
                .map(CertificatePOJO::entityToPOJO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        certificateRepository.delete(id);
    }

    @Override
    public void update(long id, CertificatePOJO certificate) {
        certificateValidator.isCorrectCertificateUpdateData(certificate);
        certificate.setModification(new Date());
        certificateRepository.update(CertificatePOJO.pojoToEntity(certificate), id);
    }

    @Override
    public void create(CertificatePOJO certificate) {
        certificateValidator.isCorrectCertificateCreateData(certificate);
        certificate.setCreationDate(new Date());
        certificateRepository.create(CertificatePOJO.pojoToEntity(certificate));
    }

    @Override
    public void addTag(long id, TagPOJO tag) {
        tagValidator.isCorrectTag(tag);
        certificateRepository.addTag(id, tagRepository.create(TagPOJO.pojoToEntity(tag)).getId());
    }

    @Override
    public void addTag(long idCertificate, long idTag) {
        certificateRepository.addTag(idCertificate,idTag);
    }

    @Override
    public void deleteTag(long idCertificate, long idTag) {
        certificateRepository.deleteTag(idCertificate,idTag);
    }

    @Override
    public List<CertificatePOJO> findByAllCertificatesByNamePart(String text) {
        text += '%';
        return certificateRepository.findAllByNamePart(text)
                .stream()
                .map(CertificatePOJO::entityToPOJO)
                .collect(Collectors.toList());
    }
}
