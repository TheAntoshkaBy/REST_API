package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificateList;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> addCertificate(@RequestBody CertificateDTO certificateDTO) {

        service.create(certificateDTO.dtoToPOJO());
        return new ResponseEntity<>(new CertificateDTO(service.create(certificateDTO.dtoToPOJO())).getModel(),
                HttpStatus.CREATED);
    }

    @GetMapping(params = {"page", "size"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByTag(@RequestBody List<TagDTO> tags,
                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "5") int size) {

        return new ResponseEntity<>(new CertificateList(service.findAllByTags(
                tags.stream()
                        .map(TagDTO::dtoToPOJO)
                        .collect(Collectors.toList()), page, size)
                .stream()
                .map(CertificateDTO::new)
                .collect(Collectors.toList()),
                service.getCertificateCount(),
                page,
                size), HttpStatus.OK);

    }

    @GetMapping(params = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(@RequestParam Map<String, String> params) {
        if (params.get("search").equals("find")) {
            return findAll(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")), "find");
        } else {
            return findComplex(params);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(HttpServletRequest params,
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

    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "5") int size,
                                     @RequestParam(value = "search", defaultValue = "find") String search) {

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

    public ResponseEntity<?> findComplex(@RequestParam Map<String, String> params) {

        return new ResponseEntity<>(new CertificateList(
                service.findAllComplex(params)
                        .stream()
                        .map(CertificateDTO::new)
                        .collect(Collectors.toList()),
                service.getCountComplex(params),
                params
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
        } catch (RepositoryException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCertificate
            (@RequestBody CertificateDTO certificate, @PathVariable int id) {

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
    public ResponseEntity<?> addTagToCertificate(@PathVariable Integer id, @RequestBody TagDTO tag) {

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
}
