package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.entity.Tag;
import com.epam.esm.pojo.CertificatePOJO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDTO {

    @Null(message = "{validation.id}")
    private Long id;

    @NotNull(message = "{validation.name.not.null}")
    @Size(min = 2, max = 70, message = "{validation.name}")
    private String name;

    @NotNull(message = "{validation.certificate.description.null}")
    @Size(min = 3, max = 170, message = "{validation.certificate.description}")
    private String description;

    @NotNull(message = "{validation.certificate.price.not.null}")
    @PositiveOrZero(message = "{validation.certificate.price}")
    private BigDecimal price;

    @NotNull(message = "{validation.certificate.duration.days.not.null}")
    @PositiveOrZero(message = "{validation.certificate.duration.days}")
    private Integer durationDays;

    @JsonIgnore
    private EntityModel<CertificateDTO> model;

    @Column(name = "date_of_creation")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    @Null(message = "{validation.certificate.date.creation}")
    private Date creationDate;

    @Column(name = "date_of_modification")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    @Null(message = "{validation.certificate.date.modification}")
    private Date modification;

    @Null(message = "{validation.certificate.tags}")
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

    public EntityModel<CertificateDTO> getModel() {
        String deleteRelName = "delete";
        String updateRelName = "update";
        String methodTypeDELETE = "DELETE";
        String methodTypePUT = "PUT";

        model = EntityModel.of(this,
            linkTo(methodOn(CertificateController.class).findById(id)).withSelfRel(),
            linkTo(methodOn(CertificateController.class).findById(id))
                .withRel(updateRelName)
                .withType(methodTypePUT),
            linkTo(methodOn(CertificateController.class).findById(id))
                .withRel(deleteRelName)
                .withType(methodTypeDELETE));
        return model;
    }
}
