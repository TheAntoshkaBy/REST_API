package com.epam.esm.service;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.TagRepository;
import com.epam.esm.repository.jpa.impl.CertificateRepositoryJPA;
import com.epam.esm.repository.jpa.impl.TagRepositoryJPA;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.ShopCertificateService;
import com.epam.esm.service.impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class CertificateServiceImplTest {

    private Certificate expectedCertificate;
    private CertificateService certificateService;
    private List<Certificate> certificates;
    private List<Tag> tags;
    private Tag tag;

    private CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler;
    private CertificateRepository certificateRepository;
    private TagRepository tagRepository;
    private CertificateValidator certificateValidator;
    private TagValidator tagValidator;

    @Before
    public void init() {
        certificateRepository = mock(CertificateRepositoryJPA.class);
        tagRepository = mock(TagRepositoryJPA.class);
        certificateValidator = mock(CertificateValidator.class);
        tagValidator = mock(TagValidator.class);
        certificateService = new ShopCertificateService();
        certificateService.setCertificateValidator(certificateValidator);
        certificateService.setCertificateRepository(certificateRepository);
        certificateService.setTagRepository(tagRepository);
        certificateService.setTagValidator(tagValidator);
        certificateServiceRequestParameterHandler = mock(CertificateServiceRequestParameterHandler.class);
        certificateService.setCertificateServiceRequestParameterHandler(mock(CertificateServiceRequestParameterHandler.class));
    }

    @Before
    public void initTagTestData() {
        long tagId = 1;

        tags = new ArrayList<>();

        tags.add(new Tag(tagId, "AllGreat"));
        tags.add(new Tag(++tagId, "LiveIsWonderful"));
        tags.add(new Tag(++tagId, "PlayTheMan"));

        tag = new Tag(++tagId, "BeStrong");
    }

    @Before
    public void initCertificateTestData() {
        final double price = 34.54;
        long idCertificate = 10;
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
    public void findAll_findAll_ActualDataMustBeEqualWithExpectedData() {
        when(certificateRepository.findAll(anyInt(), anyInt())).thenReturn(certificates);
        List<CertificatePOJO> certificatesActual = certificateService.findAll(1, 5);

        Assert.assertEquals(certificatesActual, certificates.stream()
                .map(CertificatePOJO::new)
                .collect(Collectors.toList()));
    }

    @Test
    public void findCertificateById_idCertificate_CertificateWhichContainTransmittedId() {
        Long idCertificate = 1L;
        Long foundedId = 10L;

        when(certificateRepository.findById(foundedId)).thenReturn(certificates.get(idCertificate.intValue()));

        CertificatePOJO certificate = certificateService.find(foundedId.intValue());

        Assert.assertEquals(certificate, new CertificatePOJO(certificates.get(idCertificate.intValue())));
    }

    @Test
    public void delete_IdFromExpectedCertificate_ActualCertificatesListDoesNotContainExpectedCertificate() {
        Long idCertificate = 1L;
        Long foundedId = 11L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(0);
            assertEquals(foundedId, id);
            certificates.remove(idCertificate.intValue());
            return null;
        }).when(certificateRepository).delete(anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.remove(idCertificate.intValue());

        certificateService.delete(foundedId);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void update_DataFromExpectedCertificate_ExpectedCertificateEqualWithActualCertificate() {
        Long idCertificate = 0l;
        Long foundedId = 1l;
        Long expectedId = 12l;
        Long actualId = 2l;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(idCertificate.intValue());
            Object updateCertificate = invocation.getArgument(foundedId.intValue());
            assertEquals(expectedId.intValue(), id);
            assertEquals(expectedCertificate, updateCertificate);
            certificates.set(actualId.intValue(), expectedCertificate);
            return null;
        }).when(certificateRepository).update( any(Certificate.class),anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.set(2, expectedCertificate);

        certificateService.update(12, new CertificatePOJO(expectedCertificate));

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void create_AddNewCertificate_CertificatesListActualIsContainsAddedCertificate() {
        Long idCertificate = 0L;

        when(certificateRepository.create(any(Certificate.class))).thenReturn(certificates.get(idCertificate.intValue()));

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.add(certificates.get(idCertificate.intValue()));

        certificateService.create(new CertificatePOJO(expectedCertificate));

        assertTrue(expectedCertificates.contains(certificates.get(idCertificate.intValue())));
    }

    @Test
    public void addExistingTag_AddTagByCertificateIdAndTagId_TagMustBeAddedToDatabaseAndActualCertificatesList() {
        Long id = 0L;
        Long founded = 1L;
        Long expectedId = 4L;

        tags.add(tag);

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(id.intValue());
            Object tagId = invocation.getArgument(founded.intValue());
            assertEquals(id.intValue(), certificateId);
            assertEquals(expectedId.intValue(), tagId);
            certificates.get(0).getTags().add(tags.get(3));
            return null;
        }).when(certificateRepository).addTag(anyInt(), anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.get(0).getTags().add(tags.get(3));

        certificateService.addTag(id, expectedId);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void deleteTag_DeleteTagByTagId_TgMustBeDeletedFromDatabaseAndActualCertificate() {
        Long id = 0L;
        Long founded = 1L;
        Long expectedId = 4L;
        Long actual = 3L;

        tags.add(tag);

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument(id.intValue());
            Object tagId = invocation.getArgument(founded.intValue());
            assertEquals(id, certificateId);
            assertEquals(expectedId, tagId);
            certificates.get(0).getTags().remove(tags.get(3));
            return null;
        }).when(certificateRepository).deleteTag(anyInt(), anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.get(0).getTags().remove(tags.get(actual.intValue()));

        certificateService.addTag(id, expectedId);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void findByAllCertificatesByNamePart_NamePart_CertificatesActualNotNull() {
        when(certificateRepository.findAllByNamePart(anyString()))
                .thenReturn(certificates.stream()
                        .filter(certificate -> certificate.getName().contains("ll"))
                        .collect(Collectors.toList()));

        List<Certificate> expectedCertificates = certificateService.findByAllCertificatesByNamePart("ll")
                .stream()
                .map(CertificatePOJO::pojoToEntity)
                .collect(Collectors.toList());

        Assert.assertEquals(expectedCertificates, certificates.stream()
                .filter(certificate -> certificate.getName().contains("ll"))
                .collect(Collectors.toList()));
    }

    @Test
    public void findAllWithSortByDate() {
        when(certificateRepository.findAllByDate(anyInt(),anyInt())).thenReturn(certificates);
        List<Certificate> certificatesActual = certificateService.findAllCertificatesByDate(anyInt(),anyInt())
                .stream()
                .map(CertificatePOJO::pojoToEntity)
                .collect(Collectors.toList());

        Assert.assertEquals(certificates, certificatesActual);
    }

    @Test
    public void findComplex_requestParamsAndTags_filteredResult(){
        Map<String, String> map = new HashMap<>();
        map.put("int", "1");
        Map<String, Object> mapObject = new HashMap<>();
        mapObject.put("int", 1);
        String query = "select";

        when(certificateServiceRequestParameterHandler.filterAndSetParams(map)).thenReturn(mapObject);
        when(certificateServiceRequestParameterHandler.filterAnd(map,tags.stream().map(TagPOJO::new).collect(Collectors.toList()))).thenReturn(query);
        when(certificateRepository.findAllComplex(query,mapObject,1,5)).thenReturn(certificates);
        certificates.clear();
        List<Certificate> certificatesActual = certificateService.findAllComplex(map,tags.stream().map(TagPOJO::new).collect(Collectors.toList()),1,5)
                .stream()
                .map(CertificatePOJO::pojoToEntity)
                .collect(Collectors.toList());

        Assert.assertEquals(certificates, certificatesActual);
    }

    @Test
    public void findAllWhereContainTag_Tag_CertificateWithNameEqualsExpectedTagName() {
        when(certificateRepository.findByTagName(anyString(),anyInt(),anyInt()))
                .thenReturn(certificates.stream()
                        .filter(certificate -> certificate.getTags()
                                .stream()
                                .anyMatch(tag1 -> tag1.getName().equals("Is")))
                        .collect(Collectors.toList()));

        List<Certificate> expectedCertificates = certificateService
                .findAllCertificatesByTag(new TagPOJO(tag),anyInt(),anyInt()).stream()
                .map(CertificatePOJO::pojoToEntity)
                .collect(Collectors.toList());

        Assert.assertEquals(expectedCertificates, certificates.stream()
                .filter(certificate -> certificate.getTags()
                        .stream()
                        .anyMatch(tag1 -> tag1.getName().equals("Is")))
                .collect(Collectors.toList()));
    }



    @Test
    public void setCertificateServiceRequestParameterHandler() {
        CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler =
                mock(CertificateServiceRequestParameterHandler.class);

        certificateService.setCertificateServiceRequestParameterHandler(certificateServiceRequestParameterHandler);

        Assert.assertNotNull(certificateService);
    }
}
