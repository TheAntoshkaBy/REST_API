package com.epam.esm.service.Impl;

import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CertificateServiceImpl implements CertificateService {

    private CertificateDAO certificateDAO;

    public CertificateServiceImpl(CertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO;
    }

    @Override
    public List<Certificate> getAll() {
        return certificateDAO.getAll();
    }

    @Override
    public Certificate get(Integer id) { //fixme get на find почему предпочтительнее передавать примитивы
        return certificateDAO.getCertificateById(id);
    }
}
