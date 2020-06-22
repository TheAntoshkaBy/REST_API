package com.epam.esm.service.Impl.Impl;

import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.service.Impl.CertificateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDAO certificateDAO;

    public CertificateServiceImpl(@Qualifier("certificateDAOJDBCTemplate") CertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO;
    }

    @Override
    public List<Certificate> findAll(HttpServletRequest params) throws CertificateNotFoundException {
        if (params.getParameter("filter") == null) {
            return findAll();
        }

        switch (params.getParameter("filter")) {
            case "date": {
                return findAllCertificatesByDate();
            }
            case "By name part": {
                return findByAllCertificatesByNamePart(params.getParameterValues("name")[0]);
            }
            case "more id": {
                return findAllCertificatesWhereIdMoreThenTransmittedId(
                        Integer.parseInt(params.getParameterValues("id")[0])
                );
            }
            default: {
                return findAll();
            }
        }
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
}
