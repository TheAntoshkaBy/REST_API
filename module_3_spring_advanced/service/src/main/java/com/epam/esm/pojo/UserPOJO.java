package com.epam.esm.pojo;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import lombok.*;

import java.util.List;

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

    private List<CertificateOrder> orders;

    public UserPOJO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.orders = user.getOrders();
    }

    public User pojoToEntity(){
        return new User(
                this.id,
                this.name,
                this.surname,
                this.login,
                this.password,
                this.orders
        );
    }
}
