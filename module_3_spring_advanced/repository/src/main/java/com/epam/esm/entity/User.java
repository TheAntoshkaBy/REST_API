package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "shop_user")
@Table(name = "shop_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_surname")
    private String surname;

    @Column(name = "user_login")
    private String login;

    @Column(name = "user_password")
    private String password;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CertificateOrder> orders;

    public void addOrder(CertificateOrder order) {
        orders.add(order);
        order.setOwner(this);
    }

    public void removeOrder(CertificateOrder order) {
        orders.remove(order);
        order.setOwner(null);
    }
}
