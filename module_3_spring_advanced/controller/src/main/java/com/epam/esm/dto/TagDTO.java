package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.TagController;
import com.epam.esm.pojo.TagPOJO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.EntityModel;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {

    @Null(message = "{validation.tag.id}")
    private Long id;

    @NotNull(message = "{validation.tag.name}")
    @Size(min = 2, max = 70, message = "{validation.tag.name.not.null}")
    private String name;

    @JsonIgnore
    @ToString.Exclude
    private EntityModel<TagDTO> model;

    public TagDTO(TagPOJO tag) {
        this.name = tag.getName();
        this.id = tag.getId();
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
