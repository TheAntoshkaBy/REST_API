package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificateList;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.exception.InvalidControllerOutputMessage;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("certificates")
public class CertificateController {

    private CertificateService service;

    @Autowired
    public void setService(CertificateService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCertificate(@RequestBody @Valid CertificateDTO certificateDTO) {
        service.create(certificateDTO.dtoToPOJO());
        return new ResponseEntity<>(new CertificateDTO(service.create(certificateDTO.dtoToPOJO())).getModel(),
                HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(@RequestParam Map<String, String> params, @RequestBody(required = false) List<TagDTO> tags) {
        String pageParam = "page";
        String sizeParam = "size";
        int page = getValidPaginationParam(params.get(pageParam), pageParam);
        int size = getValidPaginationParam(params.get(sizeParam), sizeParam);

        String searchParameter = params.get("search");
        String findParameter = params.get("filter");
        String sortParameter = params.get("sort");

        if (searchParameter != null && searchParameter.equals("complex")) {
            return findComplex(params, tags, page, size);
        } else {
            if (findParameter != null || sortParameter != null)
                return findByFilter(params, page, size);
            return findAll(page, size);
        }
    }

    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "5") int size) {

        CertificateList certificateList = new CertificateList(
                service.findAll(page, size)
                        .stream()
                        .map(CertificateDTO::new)
                        .collect(Collectors.toList()),
                service.getCertificateCount(),
                page,
                size
        );
        return new ResponseEntity<>(certificateList, HttpStatus.OK);

    }

    public ResponseEntity<?> findComplex(
            @RequestParam Map<String, String> params,
            @RequestBody List<TagDTO> tags,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        List<TagPOJO> tagsPojo = null;
        if (tags != null) {
            tagsPojo = tags
                    .stream()
                    .map(TagDTO::dtoToPOJO)
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(new CertificateList(
                service.findAllComplex(params, tagsPojo,
                        page,
                        size)
                        .stream()
                        .map(CertificateDTO::new)
                        .collect(Collectors.toList()),
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

    @DeleteMapping(path = "/{id}", params = {"page", "size"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCertificate(@PathVariable Integer id,
                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "5") int size) {
        try {
            service.delete(id);
            return new ResponseEntity<>(
                    new CertificateList(
                            service.findAll(page, size)
                                    .stream()
                                    .map(CertificateDTO::new)
                                    .collect(Collectors.toList()),
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
        service.update(id, certificate.dtoToPOJO());
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
        service.addTag(id, tag.dtoToPOJO());

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
        String invalid = "this parameter is invalid!";
        int defaultPage = 1;
        int defaultSize = 5;

        if (param == null) {
            if (paramName.equals("page"))
                return defaultPage;
            else
                return defaultSize;
        } else {
            int paramInteger;
            try {
                paramInteger = Integer.parseInt(param);
            } catch (RuntimeException e) {
                throw new ControllerException(
                        new InvalidControllerOutputMessage(paramName, invalid)
                );
            }
            if (paramInteger > 0)
                return paramInteger;
            else
                return defaultPage;
        }
    }


    public ResponseEntity<?> findByFilter(Map<String, String> params,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "size", defaultValue = "5") int size) {
        CertificateList certificateList = new CertificateList(
                service.findAll(params)
                        .stream()
                        .map(CertificateDTO::new)
                        .collect(Collectors.toList()),
                service.getCertificateCount(),
                page,
                size
        );
        return new ResponseEntity<>(certificateList, HttpStatus.OK);
    }
}
