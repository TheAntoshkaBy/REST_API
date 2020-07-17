package com.epam.esm.dto;

import com.epam.esm.controller.OrderController;
import com.epam.esm.entity.Certificate;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.EntityModel;

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

    private Long id;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date endTime;
    private Double coast;
    private String description;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date createdTime;

    private UserDTO owner;

    @JsonIgnore
    private EntityModel<CertificateOrderDTO> model;

    private List<Certificate> certificates;//fixme доставать только имена сертификатов

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