package com.epam.esm.dto;

import com.epam.esm.pojo.UserPOJO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

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

    public UserDTO(UserPOJO user){
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.login = user.getLogin();
        this.password = user.getPassword();
     }

    public UserPOJO dtoToPojo(){
        return new UserPOJO(
                this.name,
                this.surname,
                this.login,
                this.password
        );
    }
}
