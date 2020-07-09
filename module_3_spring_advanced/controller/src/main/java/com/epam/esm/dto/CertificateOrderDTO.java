package com.epam.esm.dto;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.pojo.CertificateOrderPOJO;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter @Setter
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class CertificateOrderDTO {
    private Long id;
    private Date timestamp;
    private Double coast;
    private User owner;

    private List<Certificate> certificates;

    public CertificateOrderDTO(CertificateOrderPOJO certificateOrderPOJO){
        this.id = certificateOrderPOJO.getId();
        this.coast = certificateOrderPOJO.getCoast();
        this.owner = certificateOrderPOJO.getOwner();
        this.timestamp = certificateOrderPOJO.getTimestamp();
        this.certificates = certificateOrderPOJO.getCertificates();
    }

    public CertificateOrderPOJO dtoToPojo(){
        return new CertificateOrderPOJO(
                this.id,
                this.timestamp,
                this.coast,
                this.owner,
                this.certificates
        );
    }
}
