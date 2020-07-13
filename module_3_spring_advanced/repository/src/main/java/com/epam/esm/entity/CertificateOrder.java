package com.epam.esm.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity(name = "certificate_order")
@Table(name = "certificate_order")
public class CertificateOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_time")
    private Date timestamp;

    @Column(name = "coast")
    private Double coast;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "o_c",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id")
    )
    private List<Certificate> certificates;

    public CertificateOrder(Long id, Date timestamp, Double summaryCoast, User owner, List<Certificate> certificates) {
        this.id = id;
        this.timestamp = timestamp;
        this.coast = summaryCoast;
        this.owner = owner;
        this.certificates = certificates;
    }

    public CertificateOrder(Long id, Date timestamp, Double summaryCoast, List<Certificate> certificates) {
        this.id = id;
        this.timestamp = timestamp;
        this.coast = summaryCoast;
        this.certificates = certificates;
    }

    public CertificateOrder() {
    }

    public CertificateOrder(Date timestamp, Double summaryCoast) {
        this.timestamp = timestamp;
        this.coast = summaryCoast;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getCoast() {
        return coast;
    }

    public void setCoast(Double summaryCoast) {
        this.coast = summaryCoast;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }
}
