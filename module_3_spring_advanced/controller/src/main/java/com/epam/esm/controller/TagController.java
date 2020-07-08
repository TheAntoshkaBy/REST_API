package com.epam.esm.controller;

import com.epam.esm.entity.CertificateDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagDTO;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.tag.TagException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/tags")
public class TagController {
    private TagService service;

    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTag(@RequestBody TagDTO tag) {
        try {
            service.create(TagDTO.dtoToPOJO(tag));
            return new ResponseEntity<>(service.findAll()
                    .stream()
                    .map(TagDTO::pojoToDTO)
                    .collect(Collectors.toList()), HttpStatus.CREATED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findTag(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(TagDTO.pojoToDTO(service.find(id)), HttpStatus.OK);
        } catch (TagException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagDTO>> findAll() {
        return new ResponseEntity<>(service.findAll()
                .stream()
                .map(TagDTO::pojoToDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Integer id) {
        try {
            service.delete(id);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.findAll()
                .stream()
                .map(TagDTO::pojoToDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
