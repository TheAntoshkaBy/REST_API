package com.epam.esm.pojo;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CertificatePOJO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer durationDays;
    private List<Tag> tags;
    private Date creationDate;
    private Date modification;

    public CertificatePOJO(Long id, String name, String description, Double price, Integer durationDays, List<Tag> tags, Date creationDate, Date modification) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
        this.tags = tags;
        this.creationDate = creationDate;
        this.modification = modification;
    }

    public CertificatePOJO(Long id, String name, String description, Double price, Date creationDate, Date modification, Integer durationDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
        this.creationDate = creationDate;
        this.modification = modification;
    }

    public CertificatePOJO(String name, String description, Double price, Integer durationDays) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
    }

    public CertificatePOJO(Certificate certificate) {
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.price = certificate.getPrice();
        this.creationDate = certificate.getCreationDate();
        this.modification = certificate.getModification();
        this.durationDays = certificate.getDurationDays();
        this.tags = certificate.getTags();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModification() {
        return modification;
    }

    public void setModification(Date modification) {
        this.modification = modification;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificatePOJO that = (CertificatePOJO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(durationDays, that.durationDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, durationDays);
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", modification=" + modification +
                ", durationDays=" + durationDays +
                '}';
    }

    public Certificate pojoToEntity() {
        return new Certificate(
                this.name,
                this.description,
                this.price,
                this.durationDays
        );
    }
}
