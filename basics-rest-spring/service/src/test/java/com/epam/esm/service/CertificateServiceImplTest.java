package com.epam.esm.service;

import com.epam.esm.dao.Impl.CertificateDAOJDBCTemplate;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.service.Impl.CertificateServiceImpl;
import com.epam.esm.service.Impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.validator.CertificateValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    private List<Tag> tags;
    private Tag tag;

    private CertificateDAOJDBCTemplate certificateDAOJDBCTemplate;
    private CertificateValidator certificateValidator;

    @Before
    public void init() {
        certificateDAOJDBCTemplate = mock(CertificateDAOJDBCTemplate.class);
        certificateValidator = mock(CertificateValidator.class);
    }

    @Before
    public void initTagTestData() {
        int tagId = 1;

        tags = new ArrayList<>();

        tags.add(new Tag(tagId, "AllGreat"));
        tags.add(new Tag(++tagId, "LiveIsWonderful"));
        tags.add(new Tag(++tagId, "PlayTheMan"));

        tag = new Tag(++tagId, "BeStrong");
    }

    @Before
    public void initCertificateTestData() {
        final double price = 34.54;
        int idCertificate = 10;
        int duration = 10;

        certificates = new ArrayList<>();

        certificates.add(new Certificate(idCertificate, "Football", "for You",
                price, new Date(), new Date(), duration, tags));
        certificates.add(new Certificate(++idCertificate, "Bolls", "for You",
                price, new Date(), new Date(), duration, tags));
        certificates.add(new Certificate(++idCertificate, "Success", "for You",
                price, new Date(), new Date(), duration, tags));
        expectedCertificate = new Certificate(idCertificate, "English level", "for You",
                price, new Date(), new Date(), duration, tags);
    }

    @Test
    public void findAll_findAll_ActualDataMustBeEqualWithExpectedData() throws CertificateNotFoundException {
        when(certificateDAOJDBCTemplate.findAll()).thenReturn(certificates);
        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        List<Certificate> certificatesActual = certificateService.findAll();

        Assert.assertEquals(certificatesActual, certificates);
    }

    @Test
    public void findCertificateById_idCertificate_CertificateWhichContainTransmittedId()
            throws CertificateNotFoundException {
        int idCertificate = 1;
        int foundedId = 10;

        when(certificateDAOJDBCTemplate.findCertificateById(foundedId)).thenReturn(certificates.get(idCertificate));

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        Certificate certificate = certificateService.find(foundedId);

        Assert.assertEquals(certificate, certificates.get(idCertificate));
    }

    @Test
    public void delete_IdFromExpectedCertificate_ActualCertificatesListDoesNotContainExpectedCertificate()
            throws CertificateNotFoundException {
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
    public void update_DataFromExpectedCertificate_ExpectedCertificateEqualWithActualCertificate()
            throws CertificateNotFoundException {
        int idCertificate = 0;
        int foundedId = 1;
        int expectedId = 12;
        int actualId = 2;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(idCertificate);
            Object updateCertificate = invocation.getArgument(foundedId);
            assertEquals(expectedId, id);
            assertEquals(expectedCertificate, updateCertificate);
            certificates.set(actualId, expectedCertificate);
            return null;
        }).when(certificateDAOJDBCTemplate).updateCertificate(anyInt(), any(Certificate.class));

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.set(2, expectedCertificate);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.setCertificateValidator(certificateValidator);
        certificateService.update(12, expectedCertificate);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void create_AddNewCertificate_CertificatesListActualIsContainsAddedCertificate()
            throws CertificateNotFoundException {
        int idCertificate = 0;

        doAnswer(invocation -> {
            Object newCertificate = invocation.getArgument(idCertificate);
            assertEquals(expectedCertificate, newCertificate);
            assertEquals(expectedCertificate, newCertificate);
            certificates.add(expectedCertificate);
            return null;
        }).when(certificateDAOJDBCTemplate).updateCertificate(anyInt(), any(Certificate.class));

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.add(expectedCertificate);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.setCertificateValidator(certificateValidator);
        certificateService.create(expectedCertificate);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void addExistingTag_AddTagByCertificateIdAndTagId_TagMustBeAddedToDatabaseAndActualCertificatesList() {
        int id = 0;
        int founded = 1;
        int expectedId = 4;

        tags.add(tag);

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(id);
            Object tagId = invocation.getArgument(founded);
            assertEquals(id, certificateId);
            assertEquals(expectedId, tagId);
            certificates.get(0).getTags().add(tags.get(3));
            return null;
        }).when(certificateDAOJDBCTemplate).addTag(anyInt(), anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.get(0).getTags().add(tags.get(3));

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.addTag(id, expectedId);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void deleteTag_DeleteTagByTagId_TgMustBeDeletedFromDatabaseAndActualCertificate()
            throws CertificateNotFoundException {
        int id = 0;
        int founded = 1;
        int expectedId = 4;
        int actual = 3;

        tags.add(tag);

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(id);
            Object tagId = invocation.getArgument(founded);
            assertEquals(id, certificateId);
            assertEquals(expectedId, tagId);
            certificates.get(0).getTags().remove(tags.get(3));
            return null;
        }).when(certificateDAOJDBCTemplate).deleteTag(anyInt(), anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.get(0).getTags().remove(tags.get(actual));

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.addTag(id, expectedId);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void findByAllCertificatesByNamePart_NamePart_CertificatesActualNotNull()
            throws CertificateNotFoundException {
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
    public void findAllWithSortByDate() throws CertificateNotFoundException {
        when(certificateDAOJDBCTemplate.findAllByDate()).thenReturn(certificates);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        List<Certificate> certificatesActual = certificateService.findAllCertificatesByDate();

        Assert.assertEquals(certificates, certificatesActual);
    }

    @Test
    public void findAllCertificatesByParameter_ParameterId_AllCertificatesWithoutTransmittedParameter() {
        int idCertificate = 11;

        when(certificateDAOJDBCTemplate.findCertificateWhereIdMoreThanParameter(anyInt()))
                .thenReturn(certificates.stream()
                        .filter(certificate -> certificate.getId() > idCertificate)
                        .collect(Collectors.toList()));

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        List<Certificate> expectedCertificates = certificateService
                .findAllCertificatesWhereIdMoreThenTransmittedId(idCertificate);

        Assert.assertEquals(expectedCertificates, certificates.stream()
                .filter(certificate -> certificate.getId() > idCertificate)
                .collect(Collectors.toList()));
    }

    @Test
    public void findAllWhereContainTag_Tag_CertificateWithNameEqualsExpectedTagName()
            throws CertificateNotFoundException {
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

    @Test
    public void findAll() throws CertificateNotFoundException {
        CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler =
                mock(CertificateServiceRequestParameterHandler.class);
        when(certificateServiceRequestParameterHandler.filter(any(HttpServletRequest.class)))
                .thenReturn(certificates);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.setCertificateServiceRequestParameterHandler(certificateServiceRequestParameterHandler);

        Assert.assertEquals(certificates, certificateService.findAll(mock(HttpServletRequest.class)));
    }

    @Test
    public void setCertificateServiceRequestParameterHandler() throws CertificateNotFoundException {
        CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler =
                mock(CertificateServiceRequestParameterHandler.class);
        when(certificateServiceRequestParameterHandler.filter(any(HttpServletRequest.class)))
                .thenReturn(certificates);

        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.setCertificateServiceRequestParameterHandler(certificateServiceRequestParameterHandler);

        Assert.assertNotNull(certificateService.findAll(mock(HttpServletRequest.class)));
    }
}