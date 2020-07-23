package com.epam.esm.dto;

import com.epam.esm.controller.OrderController;
import com.epam.esm.entity.Certificate;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.EntityModel;

import javax.validation.constraints.Null;
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
public class CertificateOrderDTO {

    @Null
    private Long id;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    @Null
    private Date endTime;
    @Null
    private Double coast;

    @Size(min = 3, max = 170, message
            = "Surname must be between 3 and 170 characters")
    private String description;

    @Null
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date createdTime;

    private UserDTO owner;

    @JsonIgnore
    private EntityModel<CertificateOrderDTO> model;

    private List<Certificate> certificates;

    public CertificateOrderDTO(CertificateOrderPOJO certificateOrderPOJO) {
        this.id = certificateOrderPOJO.getId();
        this.coast = certificateOrderPOJO.getCoast();
        this.owner = new UserDTO(certificateOrderPOJO.getOwner());
        this.endTime = certificateOrderPOJO.getEndDate();
        this.certificates = certificateOrderPOJO.getCertificates();
        this.description = certificateOrderPOJO.getDescription();
        this.createdTime = certificateOrderPOJO.getCreatedDate();
    }

    public CertificateOrderPOJO dtoToPojo() {
        return new CertificateOrderPOJO(
                this.endTime,
                this.description
        );
    }

    public EntityModel<CertificateOrderDTO> getModel() {
        model = EntityModel.of(this,
                linkTo(methodOn(OrderController.class).findOrderById(id)).withSelfRel(),
                linkTo(methodOn(OrderController.class).deleteOrder(id, 1, 5)).withRel("delete").withType("DELETE"));
        return model;
    }
}