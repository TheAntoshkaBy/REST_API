package com.epam.esm.pojo;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificatePOJO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer durationDays;
    private List<Tag> tags;
    private Date creationDate;
    private Date modification;

    public CertificatePOJO(Long id,
                           String name,
                           String description,
                           Double price,
                           Date creationDate,
                           Date modification,
                           Integer durationDays) {
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

    public Certificate pojoToEntity() {
        return new Certificate(
                this.name,
                this.description,
                this.price,
                this.durationDays
        );
    }
}
