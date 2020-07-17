package com.epam.esm.dto;

import com.epam.esm.controller.TagController;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagList {
    private CollectionModel<EntityModel<TagDTO>> tags;

    public TagList(List<TagDTO> tagList, int tagCount, int page, int size) {
        this.tags = CollectionModel.of(
                tagList
                        .stream()
                        .map(TagDTO::getModel)
                        .collect(Collectors.toList())
        );

        if (tagCount > page * size) {
            int nextPage = page + 1;
            this.tags.add(linkTo(methodOn(TagController.class)
                    .findAll(nextPage, size)).withRel("next"));
        }

        this.tags.add(linkTo(methodOn(TagController.class)
                .findAll(page, size)).withRel("current"));

        if (page != 1) {
            int prevPage = page - 1;
            this.tags.add(linkTo(methodOn(TagController.class)
                    .findAll(prevPage, size)).withRel("previous"));
        }
    }

}
