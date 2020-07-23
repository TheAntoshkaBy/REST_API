package com.epam.esm.pojo;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CertificateOrderPOJO {
    private Long id;
    private Date endDate;
    private Double coast;
    private String description;
    private Date createdDate;

    private UserPOJO owner;

    private List<Certificate> certificates;

    public CertificateOrderPOJO(CertificateOrder certificateOrder) {
        this.id = certificateOrder.getId();
        this.coast = certificateOrder.getCoast();

        if (certificateOrder.getOwner() != null) {
            this.owner = new UserPOJO(
                    certificateOrder.getOwner().getId(),
                    certificateOrder.getOwner().getName(),
                    certificateOrder.getOwner().getSurname(),
                    certificateOrder.getOwner().getLogin(),
                    certificateOrder.getOwner().getPassword(),
                    certificateOrder.getOwner().getRoles(),
                    certificateOrder.getOwner().getEmail()
            );
        } else {
            this.owner = null;
        }
        this.endDate = certificateOrder.getEndTime();
        this.certificates = certificateOrder.getCertificates();
        this.description = certificateOrder.getDescription();
        this.createdDate = certificateOrder.getCreateTime();
    }

    public CertificateOrderPOJO(Date endDate, String description) {
        this.endDate = endDate;
        this.description = description;
    }

    public CertificateOrder pojoToEntity() {
        return new CertificateOrder(
                this.endDate,
                this.coast,
                this.description,
                this.createdDate
        );
    }
}
