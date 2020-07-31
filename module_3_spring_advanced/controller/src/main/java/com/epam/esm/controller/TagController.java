package com.epam.esm.controller;

import com.epam.esm.controller.support.ControllerSupporter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagList;
import com.epam.esm.service.TagService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/tags")
public class TagController {

    private final static String PAGE_NAME_PARAMETER = "page";
    private final static String PAGE_SIZE_NAME_PARAMETER = "size";
    private final static String PAGE_DEFAULT_PARAMETER = "1";
    private final static String PAGE_SIZE_DEFAULT_PARAMETER = "5";
    private final TagService service;

    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTag(@RequestBody @Valid TagDTO tag) {

        return new ResponseEntity<>(new TagDTO(
            service.create(ControllerSupporter.tagDtoToTagPOJO(tag))
        ).getModel(), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findTag(@PathVariable Long id) {
        return new ResponseEntity<>(new TagDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(
        @RequestParam(value = PAGE_NAME_PARAMETER,
            defaultValue = PAGE_DEFAULT_PARAMETER,
            required = false) int page,
        @RequestParam(value = PAGE_SIZE_NAME_PARAMETER,
            defaultValue = PAGE_SIZE_DEFAULT_PARAMETER,
            required = false) int size) {

        return new ResponseEntity<>(
            new TagList.TagListBuilder(service.findAll(page, size))
                .resultCount(service.getTagCount())
                .page(page).size(size)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        service.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
