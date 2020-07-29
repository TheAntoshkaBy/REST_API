package com.epam.esm.controller;

import com.epam.esm.controller.support.ControllerSupporter;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificateList;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("certificates")
public class CertificateController {

    private final static String PAGE_NAME_PARAMETER = "page";
    private final static String PAGE_SIZE_NAME_PARAMETER = "size";
    private final static String PAGE_DEFAULT_PARAMETER = "1";
    private final static String PAGE_SIZE_DEFAULT_PARAMETER = "5";
    private CertificateService service;

    @Autowired
    public void setService(CertificateService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCertificate(@RequestBody @Valid CertificateDTO certificateDTO) {
        service.create(ControllerSupporter.certificateDtoToCertificatePOJO(certificateDTO));
        return new ResponseEntity<>(new CertificateDTO(
                service.create(
                        ControllerSupporter.certificateDtoToCertificatePOJO(certificateDTO))
        ).getModel(), HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(
            @RequestParam Map<String, String> params,
            @RequestBody(required = false) List<TagDTO> tags) {
        String searchRequestParameter = "search";
        String filterRequestParameter = "filter";
        String sortRequestParameter = "sort";
        String complexRequestParameter = "complex";

        int page = getValidPaginationParam(params.get(PAGE_NAME_PARAMETER), PAGE_NAME_PARAMETER);
        int size = getValidPaginationParam(params.get(PAGE_SIZE_NAME_PARAMETER), PAGE_SIZE_NAME_PARAMETER);

        String searchParameter = params.get(searchRequestParameter);
        String findParameter = params.get(filterRequestParameter);
        String sortParameter = params.get(sortRequestParameter);

        if (searchParameter != null && searchParameter.equals(complexRequestParameter)) {
            return findComplex(params, tags, page, size);
        } else {
            if (findParameter != null || sortParameter != null) {
                return findByFilter(params, page, size);
            }
            return findAll(page, size);
        }
    }

    public ResponseEntity<?> findAll(@RequestParam(value = PAGE_NAME_PARAMETER,
            defaultValue = PAGE_DEFAULT_PARAMETER) int page,
                                     @RequestParam(value = PAGE_SIZE_NAME_PARAMETER,
                                             defaultValue = PAGE_SIZE_DEFAULT_PARAMETER) int size) {

        CertificateList certificateList = new CertificateList(
                ControllerSupporter.certificatePojoListToCertificateDtoList(service.findAll(page, size)),
                service.getCertificateCount(),
                page,
                size
        );
        return new ResponseEntity<>(certificateList, HttpStatus.OK);

    }

    public ResponseEntity<?> findComplex(
            @RequestParam Map<String, String> params,
            @RequestBody List<TagDTO> tags,
            @RequestParam(value = PAGE_NAME_PARAMETER, defaultValue = PAGE_DEFAULT_PARAMETER) int page,
            @RequestParam(value = PAGE_SIZE_NAME_PARAMETER, defaultValue = PAGE_SIZE_DEFAULT_PARAMETER) int size
    ) {
        List<TagPOJO> tagsPojo = null;
        if (tags != null) {
            tagsPojo = tags
                    .stream()
                    .map(ControllerSupporter::tagDtoToTagPOJO)
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(new CertificateList(
                ControllerSupporter
                        .certificatePojoListToCertificateDtoList(
                                service.findAllComplex(params, tagsPojo, page, size)
                        ),
                service.getCountComplex(params, tagsPojo),
                params,
                page,
                size
        ), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable long id) {

        return new ResponseEntity<>(
                new CertificateDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCertificate(
            @PathVariable Integer id,
            @RequestParam(value = PAGE_NAME_PARAMETER,
                    defaultValue = PAGE_DEFAULT_PARAMETER,
                    required = false) int page,
            @RequestParam(value = PAGE_SIZE_NAME_PARAMETER,
                    defaultValue = PAGE_SIZE_DEFAULT_PARAMETER, required = false) int size) {

        try {
            service.delete(id);
            return new ResponseEntity<>(
                    new CertificateList(
                            ControllerSupporter.certificatePojoListToCertificateDtoList(service.findAll(page, size)),
                            service.getCertificateCount(),
                            page,
                            size
                    ), HttpStatus.OK);
        } catch (ControllerException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCertificate
            (@RequestBody @Valid CertificateDTO certificate, @PathVariable int id) {
        service.update(id, ControllerSupporter.certificateDtoToCertificatePOJO(certificate));
        return new ResponseEntity<>(new CertificateDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCertificatePrice
            (@RequestParam double price, @PathVariable long id) {
        service.updatePrice(id, price);
        return new ResponseEntity<>(new CertificateDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @PostMapping(path = "{id}/tags", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTagToCertificate(@PathVariable Integer id, @RequestBody @Valid TagDTO tag) {
        service.addTag(id, ControllerSupporter.tagDtoToTagPOJO(tag));

        return new ResponseEntity<>(new CertificateDTO(service.find(id)).getModel(), HttpStatus.CREATED);
    }

    @PostMapping(path = "{id}/tags/{idTag}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTagToCertificate
            (@PathVariable Integer id, @PathVariable Integer idTag) {
        service.addTag(id, idTag);

        return new ResponseEntity<>(new CertificateDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}/tags/{idTag}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTagToCertificate
            (@PathVariable Integer id, @PathVariable Integer idTag) {
        service.deleteTag(id, idTag);

        return new ResponseEntity<>(new CertificateDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    int getValidPaginationParam(String param, String paramName) {
        String pageParameter = "page";
        int defaultPage = 1;
        int defaultSize = 5;

        if (param == null) {
            if (paramName.equals(pageParameter)) {
                return defaultPage;
            } else {
                return defaultSize;
            }
        } else {
            try {
                int paramInteger = Integer.parseInt(param);
                return paramInteger > 0 ? paramInteger : defaultPage;
            } catch (NumberFormatException e) {
                return defaultPage;
            }
        }
    }

    public ResponseEntity<?> findByFilter(Map<String, String> params,
                                          @RequestParam(value = PAGE_NAME_PARAMETER,
                                                  defaultValue = PAGE_DEFAULT_PARAMETER) int page,
                                          @RequestParam(value = PAGE_SIZE_NAME_PARAMETER,
                                                  defaultValue = PAGE_SIZE_DEFAULT_PARAMETER) int size) {
        CertificateList certificateList = new CertificateList(
                ControllerSupporter.certificatePojoListToCertificateDtoList(
                service.findAll(params, page, size)),
                service.getCertificateCount(),
                page,
                size
        );
        return new ResponseEntity<>(certificateList, HttpStatus.OK);
    }
}
