package com.epam.esm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.TagRepository;
import com.epam.esm.repository.jpa.impl.CertificateRepositoryJPA;
import com.epam.esm.repository.jpa.impl.TagRepositoryJPA;
import com.epam.esm.service.impl.ShopCertificateService;
import com.epam.esm.service.impl.handler.CertificateServiceRequestParameterHandler;
import com.epam.esm.service.support.PojoConverter;
import com.epam.esm.service.support.impl.CertificatePojoConverter;
import com.epam.esm.service.support.impl.TagPojoConverter;
import com.epam.esm.service.validator.TagValidator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class ShopCertificateServiceImplTest {

    private Certificate expectedCertificate;
    private ShopCertificateService service;
    private List<Certificate> certificates;
    private List<CertificatePOJO> certificatesPOJO;
    private List<Tag> tags;
    private List<TagPOJO> tagsPOJO;
    private Tag tag;
    private Map<String, String> map;

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 5;

    private CertificateServiceRequestParameterHandler certificateServiceRequestParameterHandler;
    private CertificateRepository certificateRepository;
    private TagRepository tagRepository;
    private TagValidator tagValidator;
    private CertificatePojoConverter converter;
    private PojoConverter<TagPOJO, Tag> tagConverter;

    @Before
    public void init() {
        map = new HashMap<>();
        map.put("int", "1");

        certificateRepository = mock(CertificateRepositoryJPA.class);

        tagRepository = mock(TagRepositoryJPA.class);
        tagValidator = mock(TagValidator.class);
        converter = mock(CertificatePojoConverter.class);
        tagConverter = mock(TagPojoConverter.class);

        certificateServiceRequestParameterHandler = mock(
            CertificateServiceRequestParameterHandler.class);
        service = new ShopCertificateService(tagValidator, certificateRepository, tagRepository,
                                             converter, tagConverter);
        service
            .setCertificateServiceRequestParameterHandler(certificateServiceRequestParameterHandler);
    }

    @Before
    public void initTagTestData() {
        long tagId = 1;

        tags = new ArrayList<>();
        tagsPOJO = new ArrayList<>();

        tags.add(new Tag(tagId, "AllGreat"));
        tags.add(new Tag(++tagId, "LiveIsWonderful"));
        tags.add(new Tag(++tagId, "PlayTheMan"));

        tagsPOJO.add(new TagPOJO(tags.get(0)));
        tagsPOJO.add(new TagPOJO(tags.get(1)));
        tagsPOJO.add(new TagPOJO(tags.get(2)));

        tag = new Tag(++tagId, "BeStrong");
    }

    @Before
    public void initCertificateTestData() {
        final BigDecimal price = BigDecimal.valueOf(321.23);
        long idCertificate = 10;
        int duration = 10;

        certificates = new ArrayList<>();
        certificatesPOJO = new ArrayList<>();

        certificates.add(new Certificate(idCertificate, "Football", "for You",
                                         price, new Date(), new Date(), duration, tags));
        certificates.add(new Certificate(++idCertificate, "Bolls", "for You",
                                         price, new Date(), new Date(), duration, tags));
        certificates.add(new Certificate(++idCertificate, "Success", "for You",
                                         price, new Date(), new Date(), duration, tags));
        expectedCertificate = new Certificate(idCertificate, "English level",
                                "for You", price, new Date(), new Date(), duration, tags);

        certificatesPOJO.add(new CertificatePOJO(certificates.get(0)));
        certificatesPOJO.add(new CertificatePOJO(certificates.get(1)));
        certificatesPOJO.add(new CertificatePOJO(certificates.get(2)));
    }

    @Test
    public void findAll_findAll_ActualDataMustBeEqualWithExpectedData() {
        when(certificateRepository.findAll(anyInt(), anyInt())).thenReturn(certificates);
        when(converter.convert(anyList())).thenReturn(certificates.stream()
                                                                  .map(CertificatePOJO::new)
                                                                  .collect(Collectors.toList()));

        List<CertificatePOJO> certificatesActual = service.findAll(1, 5);

        assertEquals(certificatesActual, certificates.stream()
            .map(CertificatePOJO::new)
            .collect(Collectors.toList()));
    }

    @Test
    public void findCertificateById_idCertificate_CertificateWhichContainTransmittedId() {
        long idCertificate = 1L;
        long foundedId = 10L;

        when(certificateRepository.findById(foundedId))
            .thenReturn(certificates.get((int) idCertificate));

        CertificatePOJO certificate = service.find((int) foundedId);

        assertEquals(certificate, new CertificatePOJO(certificates.get((int) idCertificate)));
    }

    @Test
    public void delete_IdFromExpectedCertificate_ActualCertificatesNotContainExpectedCertificate() {
        long idCertificate = 1L;
        long foundedId = 11L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(0);
            assertEquals(foundedId, id);
            certificates.remove((int) idCertificate);
            return null;
        }).when(certificateRepository).delete(anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.remove((int) idCertificate);

        service.delete(foundedId);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void update_DataFromExpectedCertificate_ExpectedCertificateEqualWithActualCertificate() {
        long idCertificate = 0l;
        long foundedId = 1l;
        long expectedId = 12l;
        long actualId = 2l;

        doAnswer(invocation -> {
            Object id = invocation.getArgument((int) idCertificate);
            Object updateCertificate = invocation.getArgument((int) foundedId);
            assertEquals((int) expectedId, id);
            assertEquals(expectedCertificate, updateCertificate);
            certificates.set((int) actualId, expectedCertificate);
            return null;
        }).when(certificateRepository).update(any(Certificate.class), anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.set(2, expectedCertificate);

        service.update(12, new CertificatePOJO(expectedCertificate));

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void create_AddNewCertificate_CertificatesListActualIsContainsAddedCertificate() {
        long idCertificate = 0L;

        when(certificateRepository.create(any(Certificate.class)))
            .thenReturn(certificates.get((int) idCertificate));
        when(converter.convert(any(CertificatePOJO.class))).thenReturn(expectedCertificate);

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.add(certificates.get((int) idCertificate));

        service.create(new CertificatePOJO(expectedCertificate));

        assertTrue(expectedCertificates.contains(certificates.get((int) idCertificate)));
    }

    @Test
    public void addExistingTag_AddTagByCertificateIdAndTagId_TagMustBeAddedToDataBase() {
        long id = 0L;
        long founded = 1L;
        long expectedId = 4L;

        tags.add(tag);

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument((int) id);
            Object tagId = invocation.getArgument((int) founded);
            assertEquals((int) id, certificateId);
            assertEquals((int) expectedId, tagId);
            certificates.get(0).getTags().add(tags.get(3));
            return null;
        }).when(certificateRepository).addTag(anyInt(), anyInt());

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.get(0).getTags().add(tags.get(3));

        service.addTag(id, expectedId);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void deleteTag_DeleteTagByTagId_TgMustBeDeletedFromDatabaseAndActualCertificate() {
        long id = 0L;
        long founded = 1L;
        long expectedId = 4L;
        long actual = 3L;

        tags.add(tag);

        doAnswer(invocation -> {
            Object certificateId = invocation.getArgument((int) id);
            Object tagId = invocation.getArgument((int) founded);
            assertEquals(id, certificateId);
            assertEquals(expectedId, tagId);
            certificates.get(0).getTags().remove(tags.get(3));
            return null;
        }).when(certificateRepository).deleteTag(anyInt(), any(Tag.class));

        List<Certificate> expectedCertificates = certificates;
        expectedCertificates.get(0).getTags().remove(tags.get((int) actual));

        service.addTag(id, expectedId);

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    public void findByAllCertificatesByNamePart_NamePart_CertificatesActualNotNull() {
        String namePart = "ll";

        when(certificateRepository.findAllByNamePart(anyString()))
            .thenReturn(certificates.stream()
                .filter(certificate -> certificate.getName().contains(namePart))
                .collect(Collectors.toList()));
        when(converter.convert(anyList())).thenReturn(certificates.stream()
            .map(CertificatePOJO::new)
            .collect(Collectors.toList()));

        List<CertificatePOJO> expectedCertificates = service
            .findByAllCertificatesByNamePart(namePart);
        expectedCertificates = expectedCertificates.stream()
            .filter(certificate -> certificate.getName().contains(namePart))
            .collect(Collectors.toList());

        assertEquals(expectedCertificates, certificatesPOJO.stream()
            .filter(certificate -> certificate.getName().contains(namePart))
            .collect(Collectors.toList()));
    }

    @Test
    public void getCountComplex_withoutParams_ReturnCurrentCountOfCertificates() {
        int returnedValue = 0;
        Map<String, String> mapComplex = new HashMap<>();
        map.put("int", "1");
        Map<String, Object> mapObject = new HashMap<>();
        mapObject.put("int", 1);
        String query = "select";

        when(certificateServiceRequestParameterHandler.filterAndSetParams(mapComplex))
            .thenReturn(mapObject);
        when(certificateServiceRequestParameterHandler
            .filterAndGetCount(map, tagsPOJO))
            .thenReturn(query);
        when(certificateRepository.findCountComplex(query, mapObject)).thenReturn(returnedValue);

        int expectedValue = service.getCountComplex(map, tagsPOJO);

        assertEquals(expectedValue, returnedValue);
    }

    @Test
    public void findAllWithSortByDate_WithoutParams_ReturnAllSortedCertificatesByDate() {
        when(certificateRepository.findAllByDate(anyInt(), anyInt())).thenReturn(certificates);
        when(converter.convert(anyList())).thenReturn(certificates.stream()
            .map(CertificatePOJO::new)
            .collect(Collectors.toList()));

        List<CertificatePOJO> certificatesActual =
            service.findAllCertificatesByDate(anyInt(), anyInt());

        assertEquals(certificatesPOJO, certificatesActual);
    }

    @Test
    public void findComplex_requestParamsAndTags_filteredResult() {
        Map<String, String> map = new HashMap<>();
        map.put("int", "1");
        Map<String, Object> mapObject = new HashMap<>();
        mapObject.put("int", 1);
        String query = "select";

        when(certificateServiceRequestParameterHandler.filterAndSetParams(map))
            .thenReturn(mapObject);
        when(certificateServiceRequestParameterHandler
            .filterAnd(map, tags.stream().map(TagPOJO::new).collect(Collectors.toList())))
            .thenReturn(query);
        when(certificateRepository.findAllComplex(query, mapObject, DEFAULT_PAGE, DEFAULT_SIZE))
            .thenReturn(certificates);
        when(converter.convert(anyList())).thenReturn(certificates.stream()
            .map(CertificatePOJO::new)
            .collect(Collectors.toList()));

        List<CertificatePOJO> certificatesActual =
            service.findAllComplex(
                map, tags.stream().map(TagPOJO::new).collect(Collectors.toList()), DEFAULT_PAGE,
                                                                                    DEFAULT_SIZE);

        assertEquals(certificatesPOJO, certificatesActual);
    }

    @Test
    public void getCertificatesCount() {
        int returnedValue = 0;

        when(certificateServiceRequestParameterHandler.findAllCount(anyMap(), anyList()))
            .thenReturn(returnedValue);

        int expected = service.getCertificatesCount(map, tagsPOJO);

        assertEquals(expected, returnedValue);
    }

    @Test
    public void findByAllCertificatesByIdThresholdCount_ThresholdCount_CertificatesWithCount() {
        int id = 1;
        int count = 10;

        when(certificateRepository.findCountAllByIdThreshold(id)).thenReturn(count);

        int expected = service.findByAllCertificatesByIdThresholdCount(id);

        assertEquals(expected, count);
    }

    @Test
    public void getAllCertificateCount_WithoutParams_CertificatesCount() {
        int count = 10;

        when(certificateRepository.getCertificateCount()).thenReturn(count);

        int expected = service.getAllCertificateCount();

        assertEquals(expected, count);
    }
}
