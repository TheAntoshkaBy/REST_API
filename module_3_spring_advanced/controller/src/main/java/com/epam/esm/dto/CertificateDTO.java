package com.epam.esm.dto;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.entity.Tag;
import com.epam.esm.pojo.CertificatePOJO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.EntityModel;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer durationDays;

    @JsonIgnore
    private EntityModel<CertificateDTO> model;

    @Column(name = "date_of_creation")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date creationDate;

    @Column(name = "date_of_creation")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date modification;

    private List<Tag> tags;

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

    public EntityModel<CertificateDTO> getModel() {
        model = EntityModel.of(
                this,
                linkTo(methodOn(CertificateController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(CertificateController.class).findById(id)).withRel("update").withType("PUT"),
                linkTo(methodOn(CertificateController.class).findById(id)).withRel("delete").withType("DELETE")
        );
        return model;
    }
}