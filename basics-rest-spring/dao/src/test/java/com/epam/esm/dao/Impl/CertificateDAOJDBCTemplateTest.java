package com.epam.esm.dao.Impl;

import com.epam.esm.entity.Certificate;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

public class CertificateDAOJDBCTemplateTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    private CertificateDAOJDBCTemplate certificateDAOJDBCTemplate;

    @Mock(answer = Answers.RETURNS_MOCKS)
    JdbcTemplate jdbcTemplate;


    @Test
    public void findCertificateById_idCertificate_CertificateWhichContainThisId() {
        Integer id = 3;
        System.out.println(certificateDAOJDBCTemplate);
        Certificate certificate = certificateDAOJDBCTemplate.findCertificateById(3);
        Assert.assertEquals(certificate.getId(), id);
    }

    @Test
    public void findAll() {
    }

    @Test
    public void addCertificate() {
    }

    @Test
    public void updateCertificate() {
    }

    @Test
    public void deleteCertificateById() {
    }

    @Test
    public void addTag() {
    }

    @Test
    public void testAddTag() {
    }

    @Test
    public void deleteTag() {
    }

    @Test
    public void findCertificateWhereIdMoreThanParameter() {
    }

    @Test
    public void findCertificateWhereTagNameIs() {
    }

    @Test
    public void findCertificateByNamePart() {
    }

    @Test
    public void setTagDAO() {
    }

    public CertificateDAOJDBCTemplateTest() {
        MockitoAnnotations.initMocks(this);
        this.certificateDAOJDBCTemplate = new CertificateDAOJDBCTemplate();
    }
}