package com.epam.esm.controller;

import com.epam.esm.controller.support.impl.CertificateConverter;
import com.epam.esm.controller.support.ControllerParamNames;
import com.epam.esm.controller.support.ControllerUtils;
import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificateList;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.service.CertificateInternalService;
import com.epam.esm.service.CertificateService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("certificates")
public class CertificateController {

    private CertificateService service;
    private CertificateConverter converter;
    private DtoConverter<TagDTO, TagPOJO> tagConverter;

    @Autowired
    public CertificateController(DtoConverter<TagDTO, TagPOJO> tagConverter) {
        this.tagConverter = tagConverter;
    }

    @Autowired
    public void setConverter(
        CertificateConverter converter) {
        this.converter = converter;
    }

    @Autowired
    public void setService(
        @Qualifier("shopCertificateService") CertificateService service) {
        this.service = service;
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateDTO>>
    addCertificate(@RequestBody @Valid CertificateDTO certificateDTO) {
        service.create(converter.convert(certificateDTO));

        return new ResponseEntity<>(new CertificateDTO(
            service.create(converter.convert(certificateDTO))).getModel(), HttpStatus.CREATED);
    }

    @GetMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateList> find(@RequestParam Map<String, String> params,
        @RequestBody(required = false)
            List<TagDTO> tags) {
        int page = ControllerUtils
            .getValidPaginationParam(params.get(ControllerParamNames.PAGE_PARAM_NAME),
                ControllerParamNames.PAGE_PARAM_NAME);
        int size = ControllerUtils
            .getValidPaginationParam(params.get(ControllerParamNames.SIZE_PARAM_NAME),
                ControllerParamNames.SIZE_PARAM_NAME);

        List<TagPOJO> tagsPojo = null;
        if (tags != null) {
            tagsPojo = tags
                .stream()
                .map(tagConverter::convert)
                .collect(Collectors.toList());
        }

        List<CertificatePOJO> certificates = service
            .findAll(params, tagsPojo, page, size);
        int certificatesCount = service.getCertificatesCount(params,tagsPojo);

        CertificateList certificateList = converter.formationCertificateList(
           certificates,
            certificatesCount,
            page,
            size,
            params
        );

        return new ResponseEntity<>(certificateList, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateDTO>> findById(
        @PathVariable long id) {

        return new ResponseEntity<>(
            new CertificateDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Integer id) {
        service.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateDTO>> updateCertificate
        (@RequestBody @Valid CertificateDTO certificate, @PathVariable int id) {
        service.update(id,
            converter.convert(certificate));

        return new ResponseEntity<>(
            new CertificateDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateDTO>> updateCertificatePrice(
        @RequestParam BigDecimal price, @PathVariable long id) {
        service.updatePrice(id, price);

        return new ResponseEntity<>(
            new CertificateDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @PostMapping(path = "{id}/tags", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateDTO>> addTagToCertificate(
        @PathVariable Integer id, @RequestBody @Valid TagDTO tag) {
        service.addTag(id, tagConverter.convert(tag));

        return new ResponseEntity<>(
            new CertificateDTO(service.find(id)).getModel(),
            HttpStatus.CREATED);
    }

    @PostMapping(path = "{id}/tags/{idTag}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateDTO>> addTagToCertificate(
        @PathVariable Integer id, @PathVariable Integer idTag) {
        service.addTag(id, idTag);

        return new ResponseEntity<>(
            new CertificateDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}/tags/{idTag}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTagToCertificate(//fixme
        @PathVariable Integer id, @PathVariable Integer idTag) {
        service.deleteTag(id, idTag);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
