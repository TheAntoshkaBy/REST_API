package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService service;

    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findCertificateById(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(service.find(id), HttpStatus.OK);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(HttpServletRequest params) {
        try {
            return new ResponseEntity<>(service.findAll(params), HttpStatus.OK);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByTag(@RequestBody Tag tag) {
        try {
            return new ResponseEntity<>(service.findAllCertificatesByTag(tag), HttpStatus.OK);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCertificate(@RequestBody Certificate certificate) {
        try {
            service.create(certificate);
            return new ResponseEntity<>(service.findAll(), HttpStatus.CREATED);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "{id}/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTagToCertificate(@PathVariable Integer id, @RequestBody Tag tag) {
        try {
            service.addTag(id, tag);
            return new ResponseEntity<>(service.find(id), HttpStatus.CREATED);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "{id}/tags/{idTag}")
    public ResponseEntity<?> addTagToCertificate
            (@PathVariable Integer id, @PathVariable Integer idTag) {
        try {
            service.addTag(id, idTag);
            return new ResponseEntity<>(service.find(id), HttpStatus.OK);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCertificate
            (@RequestBody Certificate certificate, @PathVariable int id) {
        try {
            service.update(id, certificate);
            return new ResponseEntity<>(service.find(id), HttpStatus.OK);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable Integer id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "{id}/tags/{idTag}")
    public ResponseEntity<?> deleteTagToCertificate
            (@PathVariable Integer id, @PathVariable Integer idTag) {
        try {
            service.deleteTag(id, idTag);
            return new ResponseEntity<>(service.find(id), HttpStatus.OK);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
