package com.epam.esm.dto;

import com.epam.esm.controller.UserController;
import com.epam.esm.pojo.UserPOJO;
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
public class UserDTO {
    @ToString.Exclude
    private Long id;

    private String name;
    private String surname;
    private String login;
    private String password;

    @JsonIgnore
    private EntityModel<UserDTO> model;

    public UserDTO(UserPOJO user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.login = user.getLogin();
        this.password = user.getPassword();
    }

    public UserPOJO dtoToPojo() {
        return new UserPOJO(
                this.id,
                this.name,
                this.surname,
                this.login,
                this.password
        );
    }

    public EntityModel<UserDTO> getModel() {
        model = EntityModel.of(this,
                linkTo(methodOn(UserController.class).findUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).delete(id, 1, 5)).withRel("delete").withType("DELETE"),
                linkTo(methodOn(UserController.class).findOrders(id, 1, 5)).withRel("orders").withType("GET"));
        return model;
    }
}
