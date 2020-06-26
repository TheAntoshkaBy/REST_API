package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.Impl.CertificateServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class CertificateControllerTest {

    private CertificateService service;
    private CertificateController certificateController;
    private List<Tag> tags;
    private Tag testTag;
    private List<Certificate> expectedCertificates;
    private Certificate expectedCertificate;
    private ResponseEntity<?> actualResponseEntity;
    private ResponseEntity<?> expectedResponseEntity;

    @Before
    public void init() {
        service = mock(CertificateServiceImpl.class);
        certificateController = new CertificateController(service);
    }

    @Before
    public void initTestTags() {
        tags = new ArrayList<>();

        tags.add(new Tag(1, "AllGreat"));
        tags.add(new Tag(2, "LiveIsWonderful"));
        tags.add(new Tag(3, "PlayTheMan"));

        testTag = new Tag(4, "BeStrong");
    }

    @Before
    public void initTestCertificates() {
        expectedCertificates = new ArrayList<>();

        expectedCertificates.add(new Certificate(10, "Football", "for You",
                34.54, new Date(), new Date(), 10, tags));
        expectedCertificates.add(new Certificate(11, "Bolls", "for You",
                34.54, new Date(), new Date(), 10, tags));
        expectedCertificates.add(new Certificate(12, "Success", "for You",
                34.54, new Date(), new Date(), 10, tags));

        expectedCertificate = new Certificate(13, "English level", "for You",
                34.54, new Date(), new Date(), 10, tags);
    }

    @Test
    public void findCertificate_CertificateId_ResponseEntityWithCertificateWhichContainTransmittedId()
            throws ServiceException {
        when(service.find(anyInt())).thenReturn(expectedCertificate);

        actualResponseEntity = certificateController.findCertificateById(13);
        expectedResponseEntity = new ResponseEntity<>(expectedCertificate, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void findByTag_Tag_CertificatesWhichContainsTransmittedTag() throws ServiceException {
        when(service.findAllCertificatesByTag(any(Tag.class))).thenReturn(expectedCertificates);

        actualResponseEntity = certificateController.findByTag(testTag);
        expectedResponseEntity = new ResponseEntity<>(expectedCertificates, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void addCertificate_NewCertificate_ListCertificatesWhichContainTransmittedCertificate()
            throws ServiceException {
        int getFirst = 0;

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(getFirst);
            assertEquals(expectedCertificate, certificateId);
            expectedCertificates.add(expectedCertificate);
            return null;
        }).when(service).create(expectedCertificate);

        when(service.findAll()).thenReturn(expectedCertificates);

        actualResponseEntity = certificateController.findAll(any(HttpServletRequest.class));
        actualResponseEntity = certificateController.addCertificate(expectedCertificate);
        expectedResponseEntity = new ResponseEntity<>(expectedCertificates, HttpStatus.CREATED);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void updateCertificate_NewCertificateData_ListCertificatesWhichContainCertificateWithTransmittedData()
            throws ServiceException {
        int getFirst = 0;
        int getSecond = 1;

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(getFirst);
            Object certificate = invocation.getArgument(getSecond);
            assertEquals(anyInt(), certificateId);
            assertEquals(any(Certificate.class), certificate);
            expectedCertificates.set(0, expectedCertificate);
            return null;
        }).when(service).update(anyInt(), any(Certificate.class));

        when(service.find(getFirst)).thenReturn(expectedCertificates.get(getFirst));

        actualResponseEntity = certificateController.updateCertificate(any(Certificate.class), anyInt());
        expectedResponseEntity = new ResponseEntity<>(service.find(getFirst), HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void deleteCertificate_DeletedCertificate_ListCertificatesWithoutDeletedCertificate()
            throws ServiceException {
        int getFirst = 0;
        int getSecond = 1;

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(getFirst);
            assertEquals(anyInt(), certificateId);
            expectedCertificates.remove(getFirst);
            return null;
        }).when(service).delete(getSecond);

        when(service.findAll()).thenReturn(expectedCertificates);

        actualResponseEntity = certificateController.deleteCertificate(anyInt());
        expectedResponseEntity = new ResponseEntity<>(expectedCertificates, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void addTagToCertificate_NewTag_CertificateTagsWithContainsTransmittedTag()
            throws ServiceException {
        int getFirst = 0;
        int getSecond = 1;

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(getFirst);
            Object tag = invocation.getArgument(getSecond);
            assertEquals(getFirst, certificateId);
            assertEquals(testTag, tag);
            expectedCertificates.get(getFirst).getTags().add(testTag);
            return null;
        }).when(service).addTag(getFirst, testTag);

        when(service.find(getFirst)).thenReturn(expectedCertificates.get(getFirst));

        actualResponseEntity = certificateController.addTagToCertificate(getFirst, testTag);
        expectedResponseEntity = new ResponseEntity<>(service.find(getFirst), HttpStatus.CREATED);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void addTagToCertificate_idTag_CertificateTagsWithTagWhichContainsTransmittedIdTag()
            throws ServiceException {
        int getFirst = 0;
        int getSecond = 1;

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(getFirst);
            Object tagId = invocation.getArgument(getSecond);
            assertEquals(getFirst, certificateId);
            assertEquals(getFirst, tagId);
            expectedCertificates.get(getFirst).getTags().add(tags.get(getFirst));
            return null;
        }).when(service).addTag(getFirst, getFirst);

        when(service.find(getFirst)).thenReturn(expectedCertificates.get(getFirst));

        actualResponseEntity = certificateController.addTagToCertificate(anyInt(), anyInt());
        expectedResponseEntity = new ResponseEntity<>(expectedCertificates.get(getFirst), HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void deleteTagToCertificate_idTag_CertificateTagsWithoutTagWhichContainsTransmittedIdTag()
            throws ServiceException {
        int getFirst = 0;
        int getSecond = 1;

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(getFirst);
            Object tagId = invocation.getArgument(getSecond);
            assertEquals(getFirst, certificateId);
            assertEquals(getFirst, tagId);
            expectedCertificates.get(getFirst).getTags().remove(getFirst);
            return null;
        }).when(service).deleteTag(getFirst, getFirst);

        when(service.find(getFirst)).thenReturn(expectedCertificates.get(getFirst));

        actualResponseEntity = certificateController.deleteTagToCertificate(anyInt(), anyInt());
        expectedResponseEntity = new ResponseEntity<>(expectedCertificates.get(getFirst), HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }
}