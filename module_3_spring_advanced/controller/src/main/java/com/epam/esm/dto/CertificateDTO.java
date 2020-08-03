package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.support.CertificateSupporter;
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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDTO {

    @Null(message = CertificateSupporter.ERROR_ID)
    private Long id;

    @NotNull
    @Size(min = 2, max = 70, message = CertificateSupporter.ERROR_NAME)
    private String name;

    @NotNull(message = CertificateSupporter.ERROR_NULL_DESCRIPTION)
    @Size(min = 3, max = 170, message = CertificateSupporter.ERROR_DESCRIPTION)
    private String description;

    @NotNull(message = CertificateSupporter.ERROR_NULL_PRICE)
    @PositiveOrZero(message = CertificateSupporter.ERROR_PRICE)
    private BigDecimal price;

    @NotNull(message = CertificateSupporter.ERROR_NULL_DURATION_DAYS)
    @PositiveOrZero(message = CertificateSupporter.ERROR_DURATION_DAYS)
    private Integer durationDays;

    @JsonIgnore
    private EntityModel<CertificateDTO> model;

    @Column(name = "date_of_creation")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    @Null(message = CertificateSupporter.ERROR_DATE_OF_CREATION)
    private Date creationDate;

    @Column(name = "date_of_modification")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    @Null(message = CertificateSupporter.ERROR_DATE_OF_MODIFICATION)
    private Date modification;

    @Null(message = CertificateSupporter.ERROR_TAGS_LIST)
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

        model =
            EntityModel.of(
                this,
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
