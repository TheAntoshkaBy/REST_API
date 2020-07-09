package com.epam.esm.dto;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.pojo.UserPOJO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String name;
    private String surname;
    private String login;
    private String password;

    private List<CertificateOrder> orders;

    public UserDTO(UserPOJO user){
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.orders = user.getOrders();
     }

    public UserPOJO dtoToPojo(){
        return new UserPOJO(
                this.id,
                this.name,
                this.surname,
                this.login,
                this.password,
                this.orders
        );
    }
}
