package com.epam.esm.dto;

import com.epam.esm.controller.TagController;
import com.epam.esm.pojo.TagPOJO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {
    private Long id;
    private String name;

    @JsonIgnore
    private EntityModel<TagDTO> model;

    public TagDTO(TagPOJO tag) {
        this.name = tag.getName();
        this.id = tag.getId();
    }

    public TagPOJO dtoToPOJO() {
        return new TagPOJO(this.id, this.name);
    }

    public EntityModel<TagDTO> getModel() {
        model = EntityModel.of(
                this,
                linkTo(methodOn(TagController.class).findTag(id)).withSelfRel(),
                linkTo(methodOn(TagController.class).findTag(id)).withRel("delete").withType("DELETE")
        );
        return model;
    }
}
