package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/certificate")
public class CertificateController {

    private final CertificateService service;

    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> findCertificate(@PathVariable Integer id) {
        return new ResponseEntity<>(service.find(id), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> addCertificate(@RequestBody Certificate certificate) {
        service.create(certificate);
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateCertificate(@RequestBody Certificate certificate, @PathVariable int id) {
        service.update(id, certificate);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteCertificate(@PathVariable Integer id) {
        service.delete(id);
    }
}
