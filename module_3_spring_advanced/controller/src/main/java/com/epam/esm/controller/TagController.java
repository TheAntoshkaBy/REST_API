package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagList;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/tags")
public class TagController {
    private final TagService service;

    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTag(@RequestBody @Valid TagDTO tag) {

        return new ResponseEntity<>(new TagDTO(service.create(tag.dtoToPOJO())).getModel(), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findTag(@PathVariable Long id) {
        return new ResponseEntity<>(new TagDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                     @RequestParam(value = "size", defaultValue = "5", required = false) int size) {

        return new ResponseEntity<>(new TagList(
                service.findAll(page, size)
                        .stream()
                        .map(TagDTO::new)
                        .collect(Collectors.toList()),
                service.getTagCount(),
                page,
                size
        ), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Integer id,
                                       @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                       @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        service.delete(id);

        return new ResponseEntity<>(new TagList(
                service.findAll(page, size)
                        .stream()
                        .map(TagDTO::new)
                        .collect(Collectors.toList()),
                service.getTagCount(),
                page,
                size
        ), HttpStatus.OK);
    }
}
