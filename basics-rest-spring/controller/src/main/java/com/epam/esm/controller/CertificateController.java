package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.Impl.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService service;

    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> findCertificateById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.find(id), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/byDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> findAllCertificatesSortedByDate(@RequestParam String filter) {
        if(filter.equals("date")){
            return new ResponseEntity<>(service.findAllCertificatesByDate(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/more/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> findAllCertificatesWhereIdMoreTransmittedId(@PathVariable Integer id) {
        return new ResponseEntity<>(service.findAllCertificatesWhereIdMoreThenTransmittedId(id), HttpStatus.OK);
    }

    @GetMapping(path = "/byTag", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> findByTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(service.findAllCertificatesByTag(tag), HttpStatus.OK);
    }

    @GetMapping(path = "/byName")
    public ResponseEntity<List<Certificate>> findByNamePart(@RequestParam String part) {
        return new ResponseEntity<>(service.findByAllCertificatesByNamePart(part), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> addCertificate(@RequestBody Certificate certificate) {
        service.create(certificate);
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PostMapping(path = "{id}/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> addTagToCertificate(@PathVariable Integer id, @RequestBody Tag tag) {
        service.addTag(id, tag);
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PostMapping(path = "{id}/tags/{idTag}")
    public ResponseEntity<List<Certificate>> addTagToCertificate(@PathVariable Integer id, @PathVariable Integer idTag) {
        service.addTag(id, idTag);
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> updateCertificate(@RequestBody Certificate certificate, @PathVariable int id) {
        service.update(id, certificate);
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<List<Certificate>> deleteCertificate(@PathVariable Integer id) {
        service.delete(id);
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}/tags/{idTag}")
    public ResponseEntity<List<Certificate>> deleteTagToCertificate(@PathVariable Integer id, @PathVariable Integer idTag) {
        service.deleteTag(id, idTag);
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

}
