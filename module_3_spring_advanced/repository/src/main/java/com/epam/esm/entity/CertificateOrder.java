package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "certificate_order")
@Table(name = "certificate_order")
@NoArgsConstructor
@AllArgsConstructor
public class CertificateOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "description")
    private String description;


    @Column(name = "coast")
    private Double coast;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToMany(cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "o_c",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id")
    )
    private List<Certificate> certificates;

    public CertificateOrder(Date endTime, double coast, String description, Date createTime) {
        this.endTime = endTime;
        this.coast = coast;
        this.description = description;
        this.createTime = createTime;
    }
}
