package com.epam.esm.dao.Impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.constant.SQLRequests;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.exception.tag.TagNotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ContextConfiguration(locations = "classpath:test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CertificateDAOJDBCTemplateTest {

    @Autowired
    private CertificateDAOJDBCTemplate certificateDAOJDBCTemplate;
    @Autowired
    private TagDAO tagDAO;

    private List<Certificate> certificatesListExpected;
    private List<Certificate> certificatesListActual;
    private Certificate certificateExpected;
    private Certificate certificateActual;
    private Tag tagExpected;

    @Before
    public void initCertificatesTestData() throws CertificateNotFoundException {
        certificatesListExpected = new ArrayList<>();

        Certificate footballCertificateTest = new Certificate(10, "Football", "for You",
                34.54, new Date(4312), new Date(423), 10);
        certificateDAOJDBCTemplate.addCertificate(footballCertificateTest);
        certificatesListExpected.add(footballCertificateTest);

        Certificate boxingCertificateTest = new Certificate(10, "Box", "for You",
                34.54, new Date(432), new Date(53), 10);
        certificateDAOJDBCTemplate.addCertificate(boxingCertificateTest);
        certificatesListExpected.add(boxingCertificateTest);

        Certificate basketballCertificateTest = new Certificate(10, "Basketball", "for You",
                34.54, new Date(), new Date(), 10);
        certificateDAOJDBCTemplate.addCertificate(basketballCertificateTest);
        certificatesListExpected.add(basketballCertificateTest);

        certificatesListExpected = certificateDAOJDBCTemplate.findAll();

        certificateExpected = new Certificate(10, "MovieClub", "for You",
                34.54, new Date(4312), new Date(423), 10);

        certificateDAOJDBCTemplate
                .addTag(certificatesListExpected.get(0).getId(), tagDAO.addTag(new Tag(1, "AllGreat")));
        certificateDAOJDBCTemplate
                .addTag(certificatesListExpected.get(0).getId(), tagDAO.addTag(new Tag(1, "AllGreat")));
        certificateDAOJDBCTemplate
                .addTag(certificatesListExpected.get(1).getId(), tagDAO.addTag(new Tag(1, "PlayTheMan")));

        tagExpected = new Tag(2, "PlayTheMan");
    }

    @Test
    public void findCertificateById_idCertificate_CertificateWhichContainTransmittedId()
            throws CertificateNotFoundException {
        int getFirst = 0;

        certificatesListActual = certificateDAOJDBCTemplate.findAll();
        certificateExpected = certificatesListActual.get(getFirst);
        certificateActual = certificateDAOJDBCTemplate.findCertificateById(certificateExpected.getId());

        Assert.assertEquals(certificateExpected.getId(), certificateActual.getId());
    }

    @Test
    public void findAll_ActualDataMustBeEqualWithExpectedData() throws CertificateNotFoundException {
        certificatesListActual = certificateDAOJDBCTemplate.findAll();

        Assert.assertEquals(certificatesListExpected, certificatesListActual);
    }

    @Test
    public void addCertificate_AddNewCertificate_CertificatesListActualIsContainsAddedCertificate()
            throws CertificateNotFoundException {
        certificatesListExpected.add(certificateExpected);
        certificateDAOJDBCTemplate.addCertificate(certificateExpected);
        certificatesListActual = certificateDAOJDBCTemplate.findAll();

        Assert.assertTrue(certificatesListActual.contains(certificateExpected));
    }

    @Test
    public void updateCertificate_DataFromExpectedCertificate_ExpectedCertificateEqualWithActualCertificate()
            throws CertificateNotFoundException {
        int getFirst = 0;

        Certificate bufferCertificate = certificateDAOJDBCTemplate
                .findCertificateByNamePart("Football")
                .get(getFirst);

        certificateExpected.setId(bufferCertificate.getId());
        certificateDAOJDBCTemplate.updateCertificate(certificateExpected.getId(), certificateExpected);
        certificateActual = certificateDAOJDBCTemplate.findCertificateById(certificateExpected.getId());

        Assert.assertEquals(certificateExpected, certificateActual);
    }

    @Test
    public void findCertificateById_IdFromExpectedCertificate_ExpectedCertificateEqualWithActualCertificate()
            throws CertificateNotFoundException {
        int getFirst = 0;

        certificateExpected = certificateDAOJDBCTemplate.findCertificateByNamePart("Football").get(getFirst);
        certificateActual = certificateDAOJDBCTemplate.findCertificateById(certificateExpected.getId());

        Assert.assertEquals(certificateExpected, certificateActual);
    }

    @Test
    public void deleteCertificateById_IdFromExpectedCertificate_ActualCertificatesListDoesNotContainExpectedCertificate()
            throws CertificateNotFoundException {
        int getFirst = 0;

        certificateExpected = certificateDAOJDBCTemplate.findCertificateByNamePart("Football").get(getFirst);
        certificateDAOJDBCTemplate.deleteCertificateById(certificateExpected.getId());
        certificatesListActual = certificateDAOJDBCTemplate.findAll();

        Assert.assertFalse(certificatesListActual.contains(certificateExpected));
    }

    @Test
    public void findCertificateWhereIdMoreThanParameter_ParameterId_AllCertificatesIdLessTransmittedParameter() {
        int max = 300;

        certificatesListExpected = certificateDAOJDBCTemplate.findCertificateWhereIdMoreThanParameter(max);

        Assert.assertFalse(certificatesListExpected.stream().anyMatch(certificate -> certificate.getId() <= max));
    }

    @Test
    public void findCertificateByNamePart_NamePart_CertificateActualNotNull() throws CertificateNotFoundException {
        int getFirst = 0;

        certificateActual = certificateDAOJDBCTemplate.findCertificateByNamePart("Football").get(getFirst);

        Assert.assertNotNull(certificateActual);
    }

    @Test
    public void setTagDAO_TagDAONotNull() {
        Assert.assertNotNull(certificateDAOJDBCTemplate.getTagDAO());
    }

    @Test
    public void findCertificateWhereTagNameIs_Tag_CertificateWithNameEqualsExpectedTagName() {
        int getFirst = 0;

        certificateActual = certificateDAOJDBCTemplate
                .findCertificateWhereTagNameIs(tagExpected).get(getFirst);

        Assert.assertTrue(certificateActual.getTags()
                .stream()
                .anyMatch(tag -> tag.getName().equals(tagExpected.getName())));
    }

    @Test
    public void testAddTag_AddTagByCertificateIdAndTagId_TagMustBeAddedToDatabaseAndActualCertificatesList() {
        int getFirst = 0;

        certificateActual = certificateDAOJDBCTemplate.findCertificateByNamePart("Football").get(getFirst);
        certificateExpected = certificateDAOJDBCTemplate.findCertificateByNamePart("Box").get(getFirst);

        certificateDAOJDBCTemplate.addTag(certificateActual.getId(), certificateExpected.getTags().get(getFirst).getId());
        certificateActual = certificateDAOJDBCTemplate.findCertificateByNamePart("Football").get(getFirst);

        Assert.assertTrue(certificateActual.getTags().stream().anyMatch(tag -> tag.getName().equals("PlayTheMan")));
    }

    @Test
    public void deleteTag_DeleteTagByTagId_TagMustBeDeletedFromDatabaseAndActualCertificate()
            throws CertificateNotFoundException {
        int getFirst = 0;

        certificateActual = certificateDAOJDBCTemplate.findCertificateByNamePart("Football").get(getFirst);

        tagExpected = certificateActual.getTags().get(getFirst);
        certificateDAOJDBCTemplate.deleteTag(certificateActual.getId(), tagExpected.getId());
        certificateActual = certificateDAOJDBCTemplate.findCertificateByNamePart("Football").get(getFirst);

        Assert.assertTrue(certificateActual.getTags()
                .stream()
                .anyMatch(tag -> tag.getName().equals(tagExpected.getName())));
    }

    @After
    public void destroy() throws TagNotFoundException {
        certificateDAOJDBCTemplate.deleteAll();
        certificateDAOJDBCTemplate.getTagDAO().deleteAll();
        certificateDAOJDBCTemplate.getJdbcTemplate()
                .update(SQLRequests.DELETE_ALL_RELATIONSHIPS, new HashMap<>());
    }
}