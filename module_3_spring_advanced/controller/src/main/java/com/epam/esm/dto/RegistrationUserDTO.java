package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.Role;
import com.epam.esm.pojo.UserPOJO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import javax.validation.constraints.Email;
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
public class RegistrationUserDTO {

    @ToString.Exclude
    @Null
    private Long id;

    @NotNull
    @Size(min = 2, max = 70, message
        = "Name must be between 2 and 70 characters")
    private String name;

    @NotNull
    @Size(min = 3, max = 170, message
        = "Surname must be between 3 and 170 characters")
    private String surname;

    @NotNull
    @Size(min = 5, max = 30, message
        = "Login must be between 5 and 30 characters")
    private String login;

    @NotNull
    @Size(min = 4, max = 30, message
        = "Password must be between 4 and 30 characters")
    private String password;

    @Email(message = "Email should be valid")
    @NotNull
    private String email;

    @Null
    private List<Role> roles;


    @JsonIgnore
    private EntityModel<RegistrationUserDTO> model;

    public RegistrationUserDTO(UserPOJO user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.email = user.getEmail();
    }

    public EntityModel<RegistrationUserDTO> getModel(int page, int size) {
        String deleteRelName = "delete";
        String orderRelName = "orders";
        String methodTypeDELETE = "DELETE";
        String methodTypeGET = "GET";

        model = EntityModel.of(this,
            linkTo(methodOn(UserController.class)
                .findUserById(id)).withSelfRel(),
            linkTo(methodOn(UserController.class)
                .delete(id)).withRel(deleteRelName).withType(methodTypeDELETE),
            linkTo(methodOn(UserController.class)
                .findOrders(id, page, size)).withRel(orderRelName).withType(methodTypeGET));
        return model;
    }
}
