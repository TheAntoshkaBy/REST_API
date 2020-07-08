package com.epam.esm.controller;

import com.epam.esm.entity.*;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        try {
            service.create(CertificateDTO.dtoToPOJO(certificateDTO));
            return new ResponseEntity<>(new CertificateList(service.findAll()
                    .stream()
                    .map(CertificateDTO::pojoToDTO)
                    .collect(Collectors.toList())), HttpStatus.CREATED);

        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByTag(@RequestBody TagDTO tag) {
        try {
            return new ResponseEntity<>(service.findAllCertificatesByTag(TagDTO.dtoToPOJO(tag))
                    .stream()
                    .map(CertificateDTO::pojoToDTO)
                    .collect(Collectors.toList()), HttpStatus.OK);

        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(HttpServletRequest params) { //fixme заменить статик метод на конструктор пакеты переназвать дто и пожо
        try {
            return new ResponseEntity<>(new CertificateList(service.findAll(params)
                    .stream()
                    .map(CertificateDTO::pojoToDTO)
                    .collect(Collectors.toList())), HttpStatus.OK);

        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(CertificateDTO.pojoToDTO(service.find(id)), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCertificate(@PathVariable Integer id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(service.findAll()
                    .stream()
                    .map(CertificateDTO::pojoToDTO)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCertificate
            (@RequestBody CertificateDTO certificate, @PathVariable int id) {
        try {
            service.update(id, CertificateDTO.dtoToPOJO(certificate));
            return new ResponseEntity<>(service.find(id), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "{id}/tags", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTagToCertificate(@PathVariable Integer id, @RequestBody TagDTO tag) {
        try {
            service.addTag(id, TagDTO.dtoToPOJO(tag));
            return new ResponseEntity<>(CertificateDTO.pojoToDTO(service.find(id)), HttpStatus.CREATED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "{id}/tags/{idTag}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTagToCertificate
            (@PathVariable Integer id, @PathVariable Integer idTag) {
        try {
            service.addTag(id, idTag);
            return new ResponseEntity<>(CertificateDTO.pojoToDTO(service.find(id)), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "{id}/tags/{idTag}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTagToCertificate
            (@PathVariable Integer id, @PathVariable Integer idTag) {
        try {
            service.deleteTag(id, idTag);
            return new ResponseEntity<>(CertificateDTO.pojoToDTO(service.find(id)), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }
}
