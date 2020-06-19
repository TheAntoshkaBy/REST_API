package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.Impl.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/tag")
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> findTag(@PathVariable Integer id) {
        return new ResponseEntity<>(service.find(id), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Tag>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addTag(@RequestBody Tag tag) {
        service.create(tag);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteTag(@PathVariable Integer id) {
        service.delete(id);
    }
}
