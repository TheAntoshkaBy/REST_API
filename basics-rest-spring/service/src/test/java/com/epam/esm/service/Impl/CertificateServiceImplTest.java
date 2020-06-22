package com.epam.esm.service.Impl;

import com.epam.esm.dao.Impl.CertificateDAOJDBCTemplate;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.Impl.Impl.CertificateServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class CertificateServiceImplTest {

    private Certificate expectedCertificate;
    private CertificateService certificateService;
    private List<Certificate> certificates;
    private CertificateDAOJDBCTemplate certificateDAOJDBCTemplate;
    private List<Tag> tags;
    private Tag tag;

    @Before
    public void init() {
        certificateDAOJDBCTemplate = mock(CertificateDAOJDBCTemplate.class);
    }

    @Before
    public void initTagTestData(){
        int tagId = 1;

        tags = new ArrayList<>();

        tags.add(new Tag(tagId,"AllGreat"));
        tags.add(new Tag(++tagId,"LiveIsWonderful"));
        tags.add(new Tag(++tagId,"PlayTheMan"));

        tag = new Tag(++tagId,"BeStrong");
    }

    @Before
    public void initCertificateTestData(){
        final double price = 34.54;
        int idCertificate = 10;
        int duration = 10;

        certificates = new ArrayList<>();

        certificates.add(new Certificate(idCertificate, "Football", "for You",
                price, new Date(), new Date(), duration,tags));
        certificates.add(new Certificate(++idCertificate, "Bolls", "for You",
                price, new Date(), new Date(), duration, tags));
        certificates.add(new Certificate(++idCertificate, "Success", "for You",
                price, new Date(), new Date(), duration, tags));
        expectedCertificate = new Certificate(idCertificate, "English level", "for You",
                price, new Date(), new Date(), duration, tags);
    }

    @Test
    public void findAll_findAll_ActualDataMustBeEqualWithExpectedData() {
        when(certificateDAOJDBCTemplate.findAll()).thenReturn(certificates);
        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        List<Certificate> certificatesActual = certificateService.findAll();

        Assert.assertEquals(certificatesActual, certificates);
    }

    @Test
    public void findCertificateById_idCertificate_CertificateWhichContainTransmittedId() {
        int idCertificate = 1;
        int foundedId = 10;

        when(certificateDAOJDBCTemplate.findCertificateById(foundedId)).thenReturn(certificates.get(idCertificate));

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        Certificate certificate = certificateService.find(foundedId);//fixme локальные переменные вместо magic nmb

        Assert.assertEquals(certificate, certificates.get(idCertificate));
    }

    @Test
    public void delete_IdFromExpectedCertificate_ActualCertificatesListDoesNotContainExpectedCertificate() {
        int idCertificate = 1;
        int foundedId = 11;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(0);
            assertEquals(foundedId, id);
            certificates.remove(idCertificate);
            return null;
        }).when(certificateDAOJDBCTemplate).deleteCertificateById(anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.remove(idCertificate);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.delete(foundedId);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void update_DataFromExpectedCertificate_ExpectedCertificateEqualWithActualCertificate() {
        doAnswer(invocation -> {
            Object id = invocation.getArgument(0);
            Object updateCertificate = invocation.getArgument(1);
            assertEquals(12, id);
            assertEquals(expectedCertificate, updateCertificate);
            certificates.set(2, expectedCertificate);
            return null;
        }).when(certificateDAOJDBCTemplate).updateCertificate(anyInt(),any(Certificate.class));

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.set(2, expectedCertificate);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.update(12, expectedCertificate);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void create_AddNewCertificate_CertificatesListActualIsContainsAddedCertificate() {
        doAnswer(invocation -> {
            Object newCertificate = invocation.getArgument(0);
            assertEquals(expectedCertificate, newCertificate);
            assertEquals(expectedCertificate, newCertificate);
            certificates.add(expectedCertificate);
            return null;
        }).when(certificateDAOJDBCTemplate).updateCertificate(anyInt(), any(Certificate.class));

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.add(expectedCertificate);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.create(expectedCertificate);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void addTagWithCreateNewTag_AddTagByCertificateIdAndTestTagData_TagMustBeAddedToDatabaseAndActualCertificatesList() {
        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(0);
            Object newTag = invocation.getArgument(1);
            assertEquals(0, certificateId);
            assertEquals(tag, newTag);
            certificates.get(0).getTags().add(tag);
            return null;
        }).when(certificateDAOJDBCTemplate).addTag(anyInt(),any(Tag.class));

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.get(0).getTags().add(tag);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.addTag(0, tag);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void addExistingTag_AddTagByCertificateIdAndTagId_TagMustBeAddedToDatabaseAndActualCertificatesList() {
        tags.add(tag);

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(0);
            Object tagId = invocation.getArgument(1);
            assertEquals(0, certificateId);
            assertEquals(4, tagId);
            certificates.get(0).getTags().add(tags.get(3));
            return null;
        }).when(certificateDAOJDBCTemplate).addTag(anyInt(),any(Tag.class));

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.get(0).getTags().add(tags.get(3));

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.addTag(0, 4);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void deleteTag_DeleteTagByTagId_TgMustBeDeletedFromDatabaseAndActualCertificate() {
        tags.add(tag);

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(0);
            Object tagId = invocation.getArgument(1);
            assertEquals(0, certificateId);
            assertEquals(4, tagId);
            certificates.get(0).getTags().remove(tags.get(3));
            return null;
        }).when(certificateDAOJDBCTemplate).addTag(anyInt(),any(Tag.class));

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.get(0).getTags().remove(tags.get(3));

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.addTag(0, 4);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void findByAllCertificatesByNamePart_NamePart_CertificatesActualNotNull() {
        when(certificateDAOJDBCTemplate.findCertificateByNamePart(anyString()))
                .thenReturn(certificates.stream()
                        .filter(certificate -> certificate.getName().contains("ll"))
                        .collect(Collectors.toList()));
        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        List<Certificate> expectedCertificates = certificateService.findByAllCertificatesByNamePart("ll");

        Assert.assertEquals(expectedCertificates, certificates.stream()
                .filter(certificate -> certificate.getName().contains("ll"))
                .collect(Collectors.toList()));
    }

    @Test
    public void findAllWithSortByDate() {
        when(certificateDAOJDBCTemplate.findAll()).thenReturn(certificates);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificates.sort(Comparator.comparing(Certificate::getCreationDate));

        List<Certificate> certificatesActual = certificateService.findAllCertificatesByDate();

        Assert.assertEquals(certificates,certificatesActual);
    }

    @Test
    public void findAllCertificatesWhereIdMoreThanTransmittedParameter_ParameterId_AllCertificatesIdLessTransmittedParameter() {
        when(certificateDAOJDBCTemplate.findCertificateWhereIdMoreThanParameter(anyInt()))
                .thenReturn(certificates.stream()
                        .filter(certificate -> certificate.getId() > 11)
                        .collect(Collectors.toList()));

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        List<Certificate> expectedCertificates = certificateService
                .findAllCertificatesWhereIdMoreThenTransmittedId(11);

        Assert.assertEquals(expectedCertificates, certificates.stream()
                .filter(certificate -> certificate.getId() > 11)
                .collect(Collectors.toList()));
    }

    @Test
    public void findAllWhereContainTag_Tag_CertificateWithNameEqualsExpectedTagName() {
        when(certificateDAOJDBCTemplate.findCertificateWhereTagNameIs(any(Tag.class)))
                .thenReturn(certificates.stream()
                        .filter(certificate -> certificate.getTags()
                                .stream()
                                .anyMatch(tag1 -> tag1.getName().equals("Is")))
                        .collect(Collectors.toList()));

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        List<Certificate> expectedCertificates = certificateService
                .findAllCertificatesByTag(tag);

        Assert.assertEquals(expectedCertificates, certificates.stream()
                .filter(certificate -> certificate.getTags()
                        .stream()
                        .anyMatch(tag1 -> tag1.getName().equals("Is")))
                .collect(Collectors.toList()));
    }

}