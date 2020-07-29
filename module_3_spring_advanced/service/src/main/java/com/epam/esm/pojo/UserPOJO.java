package com.epam.esm.pojo;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPOJO {
    private Long id;

    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
    private List<Role> roles;

    public UserPOJO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }

    public UserPOJO(String name, String surname, String login, String password, List<Role> roles, String email) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.email = email;
    }

    public UserPOJO(
            long id,
            String name,
            String surname,
            String login,
            String password,
            List<Role> roles,
            String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.email = email;
    }
}

