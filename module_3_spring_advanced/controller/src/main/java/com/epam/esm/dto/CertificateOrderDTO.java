package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.support.impl.CertificateConverter;
import com.epam.esm.entity.Certificate;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateOrderDTO {

    @Null(message = "{validation.order.id}")
    private Long id;

    @Null(message = "{validation.order.cost}")
    private BigDecimal cost;

    @Size(min = 3, max = 170, message = "{validation.order.description}")
    private String description;

    @Null(message = "{validation.order.created.time}")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date createdTime;

    @Null(message = "{validation.order.owner}")
    private UserDTO owner;

    @JsonIgnore
    private EntityModel<CertificateOrderDTO> model;

    @Null(message = "{validation.order.certificates}")
    private List<EntityModel<CertificateDTO>> certificates;


    public CertificateOrderDTO(CertificateOrderPOJO certificateOrderPOJO) {
        CertificateConverter certificateConverter = new CertificateConverter();
        certificates = new ArrayList<>();
        this.id = certificateOrderPOJO.getId();
        this.cost = certificateOrderPOJO.getCost();
        this.owner = new UserDTO(certificateOrderPOJO.getOwner());
        if(certificateOrderPOJO.getCertificates() != null){
            certificateConverter
                .convert(certificateOrderPOJO.getCertificates())
                .forEach(certificateDTO -> {
                    certificates.add(certificateDTO.getModel());
                });
        }
        this.description = certificateOrderPOJO.getDescription();
        this.createdTime = certificateOrderPOJO.getCreatedDate();
    }

    public EntityModel<CertificateOrderDTO> getModel() {
        String deleteRelName = "delete";
        String methodType = "DELETE";
        String methodTypeGET = "GET";

        model =
            EntityModel.of(
                this,
                linkTo(methodOn(OrderController.class).findOrderById(id))
                    .withSelfRel()
                    .withType(methodTypeGET),
                linkTo(methodOn(OrderController.class).deleteOrder(id))
                    .withRel(deleteRelName)
                    .withType(methodType));
        return model;
    }
}
