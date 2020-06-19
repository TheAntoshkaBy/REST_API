package com.epam.esm.service.Impl.Impl;

import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.Impl.CertificateService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDAO certificateDAO;

    public CertificateServiceImpl(CertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO;
    }

    @Override
    public List<Certificate> findAll() {
        return certificateDAO.findAll();
    }

    @Override
    public Certificate find(int id) {
        return certificateDAO.findCertificateById(id);
    }

    @Override
    public void delete(int id) {
        certificateDAO.deleteCertificateById(id);
    }

    @Override
    public void update(int id, Certificate certificate) {
        certificateDAO.updateCertificate(id, certificate);
    }

    @Override
    public void create(Certificate certificate) {
        certificateDAO.addCertificate(certificate);
    }

    @Override
    public void addTag(int id, Tag tag) {
        certificateDAO.addTag(id, tag);
    }

    @Override
    public void addTag(int idCertificate, int idTag) {
        certificateDAO.addTag(idCertificate, idTag);
    }

    @Override
    public void deleteTag(int idCertificate, int idTag) {
        certificateDAO.deleteTag(idCertificate, idTag);
    }

    @Override
    public List<Certificate> findByAllCertificatesByNamePart(String text) {
        text+='%';
        return certificateDAO.findCertificateByNamePart(text);
    }

    @Override
    public List<Certificate> findAllCertificatesSortedByDate() { //fixme findAllByDate примеры сервисных запросов
        List<Certificate> certificates = certificateDAO.findAll();
        certificates.sort(Comparator.comparing(Certificate::getCreationDate));//fixme сортировка в бд
        return certificates;
    }

    @Override
    public List<Certificate> findAllCertificateWhereIdCountMoreThenParameterCount(int id) { //fixme семантика
        return certificateDAO.findCertificateWhereIdMoreThanParameter(id);
    }

    @Override
    public List<Certificate> findAllCertificatesWhichContainsParameterTag(Tag tag) { //fixme findAllByTag
        return certificateDAO.findCertificateWhereTagNameIs(tag);
    }
    //fixme избавиться от лишних постых строк
}
