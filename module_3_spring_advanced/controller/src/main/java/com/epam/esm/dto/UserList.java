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
    private final String nextPageModelParam = "next";
    @JsonIgnore
    private final String previousPageModelParam = "previous";
    @JsonIgnore
    private final String currentPageModelParam = "current";
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
                    .findAll(nextPage, size)).withRel(nextPageModelParam));
        }

        this.users.add(linkTo(methodOn(UserController.class)
                .findAll(page, size)).withRel(currentPageModelParam));

        if (page != 1) {
            int prevPage = page - 1;
            this.users.add(linkTo(methodOn(UserController.class)
                    .findAll(prevPage, size)).withRel(previousPageModelParam));
        }
    }
}
