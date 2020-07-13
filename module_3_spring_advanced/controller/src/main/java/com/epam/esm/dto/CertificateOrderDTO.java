package com.epam.esm.dto;

import com.epam.esm.entity.Certificate;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter @Setter
@EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateOrderDTO {

    @ToString.Exclude
    private Long id;
    private Date timestamp;
    private Double coast;

    private UserDTO owner;

    private List<Certificate> certificates;

    public CertificateOrderDTO(CertificateOrderPOJO certificateOrderPOJO){
        this.id = certificateOrderPOJO.getId();
        this.coast = certificateOrderPOJO.getCoast();
        this.owner = new UserDTO(certificateOrderPOJO.getOwner());
        this.timestamp = certificateOrderPOJO.getTimestamp();
        this.certificates = certificateOrderPOJO.getCertificates();
    }

    public CertificateOrderPOJO dtoToPojo(){
        return new CertificateOrderPOJO(
                this.timestamp,
                this.coast
        );
    }
}
