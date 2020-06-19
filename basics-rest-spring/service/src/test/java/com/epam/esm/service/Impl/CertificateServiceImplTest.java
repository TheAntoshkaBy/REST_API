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

    Certificate expectedCertificate;
    private CertificateService certificateService;
    private List<Certificate> certificates;
    private CertificateDAOJDBCTemplate certificateDAOJDBCTemplate;
    private List<Tag> tags;
    private Tag tag;

    @Before
    public void init() {
        certificateDAOJDBCTemplate = mock(CertificateDAOJDBCTemplate.class);

        tags = new ArrayList<>();
        tags.add(new Tag(1,"AllGreat"));
        tags.add(new Tag(2,"LiveIsWonderful"));
        tags.add(new Tag(3,"PlayTheMan"));
        tag = new Tag(4,"BeStrong");
        certificates = new ArrayList<>();
        certificates.add(new Certificate(10, "Football", "for You",
                34.54, new Date(), new Date(), 10,tags));
        certificates.add(new Certificate(11, "Bolls", "for You",
                34.54, new Date(), new Date(), 10, tags));
        certificates.add(new Certificate(12, "Success", "for You",
                34.54, new Date(), new Date(), 10, tags));
        expectedCertificate = new Certificate(13, "English level", "for You",
                34.54, new Date(), new Date(), 10, tags);
        //fixme отдельные методы для заполнения тестовых данных,
    }

    @Test
    public void findAll() {
        when(certificateDAOJDBCTemplate.findAll()).thenReturn(certificates);
        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);

        List<Certificate> certificatesActual = certificateService.findAll();

        Assert.assertEquals(certificatesActual, certificates);
    }

    @Test
    public void find() {
        when(certificateDAOJDBCTemplate.findCertificateById(10)).thenReturn(certificates.get(1));
        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        Certificate certificate = certificateService.find(10);//fixme локальные переменные вместо magic nmb
        Assert.assertEquals(certificate, certificates.get(1));
    }

    @Test
    public void delete() {
        doAnswer(invocation -> {
            Object id = invocation.getArgument(0);
            assertEquals(11, id);
            certificates.remove(1);
            return null;
        }).when(certificateDAOJDBCTemplate).deleteCertificateById(anyInt());
        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.remove(1);
        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        certificateService.delete(11);
        assertEquals(expectedCertificates, certificates);
    }
    //fixme разделение на логические блоки + поснить почему выбрал doAnswer

    @Test
    public void update() {
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
    public void create() {
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
    public void addTagWithCreateNewTag() {
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
    public void addExistingTag() {
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
    public void deleteTag() {
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
    public void findByAllCertificatesByNamePart() {
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
        List<Certificate> certificatesActual = certificateService.findAllCertificatesSortedByDate();
        Assert.assertEquals(certificates,certificatesActual);
    }

    @Test
    public void findAllWhereIdMoreThanParameter() {
        when(certificateDAOJDBCTemplate.findCertificateWhereIdMoreThanParameter(anyInt()))
                .thenReturn(certificates.stream()
                        .filter(certificate -> certificate.getId() > 11)
                        .collect(Collectors.toList()));
        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        List<Certificate> expectedCertificates = certificateService
                .findAllCertificateWhereIdCountMoreThenParameterCount(11);
        Assert.assertEquals(expectedCertificates, certificates.stream()
                .filter(certificate -> certificate.getId() > 11)
                .collect(Collectors.toList()));
    }

    @Test
    public void findAllWhereContainTag() {
        when(certificateDAOJDBCTemplate.findCertificateWhereTagNameIs(any(Tag.class)))
                .thenReturn(certificates.stream()
                        .filter(certificate -> certificate.getTags()
                                .stream()
                                .anyMatch(tag1 -> tag1.getName().equals("Is")))
                        .collect(Collectors.toList()));
        certificateService = new CertificateServiceImpl(certificateDAOJDBCTemplate);
        List<Certificate> expectedCertificates = certificateService
                .findAllCertificatesWhichContainsParameterTag(tag);
        Assert.assertEquals(expectedCertificates, certificates.stream()
                .filter(certificate -> certificate.getTags()
                        .stream()
                        .anyMatch(tag1 -> tag1.getName().equals("Is")))
                .collect(Collectors.toList()));
    }
}