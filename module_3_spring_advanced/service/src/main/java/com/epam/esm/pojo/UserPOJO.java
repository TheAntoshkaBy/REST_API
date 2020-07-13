package com.epam.esm.pojo;

import com.epam.esm.entity.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserPOJO {
    private Long id;

    private String name;
    private String surname;
    private String login;
    private String password;

    public UserPOJO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.login = user.getLogin();
        this.password = user.getPassword();
    }

    public UserPOJO(Long id, String name, String surname, String login) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
    }

    public UserPOJO(String name, String surname, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }

    public User pojoToEntity() {
        return new User(
                this.name,
                this.surname,
                this.login,
                this.password
        );
    }
}

