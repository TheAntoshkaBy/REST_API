package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.sun.org.glassfish.gmbal.ParameterNames;
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

    @GetMapping(path = "/byDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> sortedCertificatesByDate() {
        return new ResponseEntity<>(service.findAllWithSortByDate(), HttpStatus.OK);
    }

    @GetMapping(path = "/more/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> certificatesWhereIdMoreThanParameterId(@PathVariable Integer id) {
        return new ResponseEntity<>(service.findAllWhereIdMoreThanParameter(id), HttpStatus.OK);
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

    @PostMapping(path = "{id}/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addTagToCertificate(@PathVariable Integer id, @RequestBody Tag tag) {
        service.addTag(id, tag);
    }

    @PostMapping(path = "{id}/tags/{idTag}")
    public void addTagToCertificate(@PathVariable Integer id, @PathVariable Integer idTag) {
        service.addTag(id, idTag);
    }

    @DeleteMapping(path = "{id}/tags/{idTag}")
    public void deleteTagToCertificate(@PathVariable Integer id, @PathVariable Integer idTag) {
        service.deleteTag(id, idTag);
    }

    @GetMapping(path = "/findByTag", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> findByTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(service.findAllWhereContainTag(tag), HttpStatus.OK);
    }

    @GetMapping(path = "/findByName")
    public ResponseEntity<List<Certificate>> findByNamePart(@RequestParam String part) {
        return new ResponseEntity<>(service.findByNamePart(part), HttpStatus.OK);
    }
}
