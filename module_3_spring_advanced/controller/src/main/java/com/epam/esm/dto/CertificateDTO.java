package com.epam.esm.dto;

import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer durationDays;

    @Column(name = "date_of_creation")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date creationDate;

    @Column(name = "date_of_creation")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date modification;

    private List<Tag> tags;

    public CertificateDTO(Long id, String name, String description, Double price,
                          Date creationDate, Date modification, Integer durationDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.creationDate = creationDate;
        this.modification = modification;
        this.durationDays = durationDays;
    }

    public CertificateDTO(Long id, String name, String description, Double price,
                          Date creationDate, Date modification, Integer durationDays, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.creationDate = creationDate;
        this.modification = modification;
        this.durationDays = durationDays;
        this.tags = tags;
    }

    public CertificateDTO(CertificatePOJO certificate) {
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.price = certificate.getPrice();
        this.creationDate = certificate.getCreationDate();
        this.modification = certificate.getModification();
        this.durationDays = certificate.getDurationDays();
        this.tags = certificate.getTags();
    }

    public CertificateDTO() {
    }

    public CertificatePOJO dtoToPOJO() {
        return new CertificatePOJO(
                this.id,
                this.name,
                this.description,
                this.price,
                this.durationDays,
                this.tags,
                this.creationDate,
                this.modification
        );
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
        CertificateDTO that = (CertificateDTO) o;
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
}