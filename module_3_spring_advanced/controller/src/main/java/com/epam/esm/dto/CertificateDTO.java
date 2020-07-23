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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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

    @Null
    private Long id;

    @NotNull
    @Size(min = 2, max = 70, message
            = "Name must be between 2 and 70 characters")
    private String name;

    @NotNull
    @Size(min = 3, max = 170, message
            = "Surname must be between 3 and 170 characters")
    private String description;

    @NotNull
    @PositiveOrZero
    private Double price;

    @NotNull
    @PositiveOrZero
    private Integer durationDays;

    @JsonIgnore
    private EntityModel<CertificateDTO> model;

    @Column(name = "date_of_creation")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    @Null
    private Date creationDate;

    @Column(name = "date_of_creation")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    @Null
    private Date modification;

    @Null
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
                this.name,
                this.description,
                this.price,
                this.durationDays
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