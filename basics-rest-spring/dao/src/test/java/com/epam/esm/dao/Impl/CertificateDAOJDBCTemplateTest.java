package com.epam.esm.dao.Impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ContextConfiguration(locations = "classpath:test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CertificateDAOJDBCTemplateTest {

    @Autowired
    CertificateDAOJDBCTemplate certificateDAOJDBCTemplate;

    @Before
    public void init(){

        Certificate certificate = new Certificate(10, "Football", "for You",
                34.54, new Date(4312), new Date(423), 10);
        certificateDAOJDBCTemplate.addCertificate(certificate);

        Certificate certificate2 = new Certificate(10, "Box", "for You",
                34.54, new Date(432), new Date(53), 10);
        certificateDAOJDBCTemplate.addCertificate(certificate2);

        Certificate certificate3 = new Certificate(10, "Basketball", "for You",
                34.54, new Date(), new Date(), 10);
        certificateDAOJDBCTemplate.addCertificate(certificate3);

        certificateDAOJDBCTemplate.addTag(1, new Tag(1,"AllGreat"));
        certificateDAOJDBCTemplate.addTag(2, new Tag(2,"LiveIsWonderful"));
        certificateDAOJDBCTemplate.addTag(3, new Tag(3,"PlayTheMan"));
    }

    @After
    public void destroy(){
        certificateDAOJDBCTemplate.deleteAll();
        certificateDAOJDBCTemplate.getTagDAO().deleteAll();
    }

    @Test
    public void findCertificateById_idCertificate_CertificateWhichContainThisId() {
       List<Certificate> certificates = certificateDAOJDBCTemplate.findAll();
        System.out.println(certificates);
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

    @Test
    public void findCertificateById() {
    }

    @Test
    public void testFindAll() {
    }

    @Test
    public void testAddCertificate() {
    }

    @Test
    public void testUpdateCertificate() {
    }

    @Test
    public void testDeleteCertificateById() {
    }

    @Test
    public void testAddTag1() {
    }

    @Test
    public void testAddTag2() {
    }

    @Test
    public void testDeleteTag() {
    }

    @Test
    public void testFindCertificateWhereIdMoreThanParameter() {
    }

    @Test
    public void testFindCertificateWhereTagNameIs() {
    }

    @Test
    public void testFindCertificateByNamePart() {
    }

    @Test
    public void testSetTagDAO() {
    }
}