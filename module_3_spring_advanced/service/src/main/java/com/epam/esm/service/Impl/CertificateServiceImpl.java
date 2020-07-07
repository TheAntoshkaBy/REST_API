package com.epam.esm.service.Impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.repository.jpa.impl.CertificateJPQLJPARepository;
import com.epam.esm.repository.jpa.impl.TagJPAJPQLRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.Impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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
    public List<Certificate> findAll(HttpServletRequest params) {
        return certificateServiceRequestParameterHandler.find(params);
    }

    @Override
    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }

    @Override
    public Certificate find(long id) {
        return certificateRepository.findById(id);
    }

    @Override
    public List<Certificate> findAllCertificatesByDate() {
        return certificateRepository.findAllByDate();
    }

    @Override
    public List<Certificate> findAllCertificatesByIdThreshold(long id) {
        return certificateRepository.findAllByIdThreshold(id);
    }

    @Override
    public List<Certificate> findAllCertificatesByTag(Tag tag) {
        return certificateRepository.findByTagName(tag.getName());
    }

    @Override
    public void delete(long id) {
        certificateRepository.delete(id);
    }

    @Override
    public void update(long id, Certificate certificate) {
        certificateValidator.isCorrectCertificateUpdateData(certificate);
        certificate.setModification(new Date());
        certificateRepository.update(certificate, id);
    }

    @Override
    public void create(Certificate certificate) {
        certificateValidator.isCorrectCertificateCreateData(certificate);
        certificate.setCreationDate(new Date());
        certificateRepository.create(certificate);
    }

    @Override
    public void addTag(long id, Tag tag) {
        tagValidator.isCorrectTag(tag);
        certificateRepository.addTag(id, tagRepository.create(tag).getId());
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
    public List<Certificate> findByAllCertificatesByNamePart(String text) {
        text += '%';
        return certificateRepository.findAllByNamePart(text);
    }
}
