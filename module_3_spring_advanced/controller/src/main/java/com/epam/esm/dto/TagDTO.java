package com.epam.esm.dto;

import com.epam.esm.controller.TagController;
import com.epam.esm.pojo.TagPOJO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.hateoas.EntityModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {

    @Null
    private Long id;

    @NotNull
    @Size(min = 2, max = 70, message
            = "Name must be between 2 and 70 characters")
    private String name;

    @JsonIgnore
    @ToString.Exclude
    private EntityModel<TagDTO> model;

    public TagDTO(TagPOJO tag) {
        this.name = tag.getName();
        this.id = tag.getId();
    }

    public TagPOJO dtoToPOJO() {
        return new TagPOJO(this.id, this.name);
    }

    public EntityModel<TagDTO> getModel() {
        String deleteRelName = "delete";
        String methodTypeDELETE = "DELETE";

        model = EntityModel.of(
                this,
                linkTo(methodOn(TagController.class)
                        .findTag(id)).withSelfRel(),
                linkTo(methodOn(TagController.class)
                        .findTag(id)).withRel(deleteRelName).withType(methodTypeDELETE)
        );
        return model;
    }
}
