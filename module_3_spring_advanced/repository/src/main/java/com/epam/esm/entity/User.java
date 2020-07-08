package com.epam.esm.entity;

import javax.persistence.*;
import java.util.List;

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

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CertificateOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<CertificateOrder> orders) {
        this.orders = orders;
    }
}
