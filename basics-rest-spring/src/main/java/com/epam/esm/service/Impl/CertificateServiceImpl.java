package com.epam.esm.service.Impl;

import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDAO certificateDAO;

    public CertificateServiceImpl(CertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO;
    }

    @Override
    public List<Certificate> findAll() {
        return certificateDAO.getAll();
    }

    @Override
    public Certificate find(int id) {
        return certificateDAO.getCertificateById(id);
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
}
