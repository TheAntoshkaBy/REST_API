package com.epam.esm.service.Impl;

import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.Impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDAO certificateDAO;
    private TagDAO tagDAO;
    private CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler;
    private CertificateValidator certificateValidator;
    private TagValidator tagValidator;

    public CertificateServiceImpl
            (@Qualifier("certificateDAOJDBCTemplate") CertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO;
    }

    @Autowired
    public void setCertificateValidator(CertificateValidator certificateValidator) {
        this.certificateValidator = certificateValidator;
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
    public List<Certificate> findAll(HttpServletRequest params) throws CertificateNotFoundException {
        return certificateServiceRequestParameterHandler.filter(params);
    }

    @Override
    public List<Certificate> findAll() throws CertificateNotFoundException {
        return certificateDAO.findAll();
    }

    @Override
    public Certificate find(int id) throws CertificateNotFoundException {
        return certificateDAO.findCertificateById(id);
    }

    @Override
    public void delete(int id) throws CertificateNotFoundException {
        certificateDAO.deleteCertificateById(id);
    }

    @Override
    public void update(int id, Certificate certificate) throws CertificateNotFoundException {
        certificateValidator.isCorrectCertificateData(certificate);
        certificateDAO.updateCertificate(id, certificate);
    }

    @Override
    public void create(Certificate certificate) {
        certificateValidator.isCorrectCertificateData(certificate);
        certificateDAO.addCertificate(certificate);
    }

    @Override
    public void addTag(int id, Tag tag) {
        tagValidator.isCorrectTag(tag);
        certificateDAO.addTag(id, tagDAO.addTag(tag));
    }

    @Override
    public void addTag(int idCertificate, int idTag) {
        certificateDAO.addTag(idCertificate, idTag);
    }

    @Override
    public void deleteTag(int idCertificate, int idTag) throws CertificateNotFoundException {
        certificateDAO.deleteTag(idCertificate, idTag);
    }

    @Override
    public List<Certificate> findByAllCertificatesByNamePart(String text) throws CertificateNotFoundException {
        text += '%';
        return certificateDAO.findCertificateByNamePart(text);
    }

    @Override
    public List<Certificate> findAllCertificatesByDate() throws CertificateNotFoundException {
        return certificateDAO.findAllByDate();
    }

    @Override
    public List<Certificate> findAllCertificatesWhereIdMoreThenTransmittedId(int id) {
        return certificateDAO.findCertificateWhereIdMoreThanParameter(id);
    }

    @Override
    public List<Certificate> findAllCertificatesByTag(Tag tag) throws CertificateNotFoundException {
        return certificateDAO.findCertificateWhereTagNameIs(tag);
    }

    @Autowired
    public void setTagDAO(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Autowired
    public void setCertificateServiceRequestParameterHandler
            (CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler) {
        this.certificateServiceRequestParameterHandler = certificateServiceRequestParameterHandler;
    }
}
