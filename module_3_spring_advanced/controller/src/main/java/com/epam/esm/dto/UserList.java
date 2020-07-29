package com.epam.esm.dto;

import com.epam.esm.controller.UserController;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserList {
    @JsonIgnore
    private final static String NEXT_PAGE_MODEL_PARAM = "next";
    @JsonIgnore
    private final static String PREVIOUS_PAGE_MODEL_PARAM = "previous";
    @JsonIgnore
    private final static String CURRENT_PAGE_MODEL_PARAM = "current";
    private CollectionModel<EntityModel<UserDTO>> users;

    public UserList(List<UserDTO> usersDTO, int tagCount, int page, int size) {
        this.users = CollectionModel.of(
                usersDTO
                        .stream()
                        .map(UserDTO::getModel)
                        .collect(Collectors.toList())
        );

        if (tagCount > page * size) {
            int nextPage = page + 1;
            this.users.add(linkTo(methodOn(UserController.class)
                    .findAll(nextPage, size)).withRel(NEXT_PAGE_MODEL_PARAM));
        }

        this.users.add(linkTo(methodOn(UserController.class)
                .findAll(page, size)).withRel(CURRENT_PAGE_MODEL_PARAM));

        if (page != 1) {
            int prevPage = page - 1;
            this.users.add(linkTo(methodOn(UserController.class)
                    .findAll(prevPage, size)).withRel(PREVIOUS_PAGE_MODEL_PARAM));
        }
    }
}
