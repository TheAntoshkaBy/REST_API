package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.support.ControllerParamNames;
import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagList;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.service.TagService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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

    private final TagService service;
    private final DtoConverter<TagDTO, TagPOJO> converter;

    @Autowired
    public TagController(TagService service, DtoConverter<TagDTO, TagPOJO> converter) {
        this.service = service;
        this.converter = converter;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<TagDTO>> addTag(@RequestBody @Valid TagDTO tag) {
        TagDTO resultTag = new TagDTO(service.create(converter.convert(tag)));

        return ResponseEntity.created(linkTo(methodOn(TagController.class)
            .findTag(resultTag.getId()))
            .toUri()).body(resultTag.getModel());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<TagDTO>> findTag(@PathVariable Long id) {
        return new ResponseEntity<>(new TagDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagList> findAll(
        @RequestParam(value = ControllerParamNames.PAGE_PARAM_NAME,
            defaultValue = ControllerParamNames.DEFAULT_PAGE_STRING,
            required = false) int page,
        @RequestParam(value = ControllerParamNames.SIZE_PARAM_NAME,
            defaultValue = ControllerParamNames.DEFAULT_SIZE_STRING,
            required = false) int size) {

        List<TagPOJO> tagPOJOList = service.findAll(page, size);
        int tagsCount = service.getTagCount();

        return new ResponseEntity<>(
            new TagList.TagListBuilder(tagPOJOList, converter)
                .resultCount(tagsCount)
                .page(page).size(size)
                .build(), HttpStatus.OK);
    }

    @GetMapping(path = "/tags/popular",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> findMostWidelyUsedTagByMostActiveUser() {
        TagDTO mostWidelyUserTag = new TagDTO(service.findMostWidelyUsedTag());

        return new ResponseEntity<>(mostWidelyUserTag, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        service.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
