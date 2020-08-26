package com.epam.esm.pojo;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.service.support.impl.CertificatePojoConverter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateOrderPOJO {

    private Long id;
    private BigDecimal cost;
    private String description;
    private Date createdDate;

    private UserPOJO owner;

    private List<CertificatePOJO> certificates;

    public CertificateOrderPOJO(CertificateOrder certificateOrder) {
        CertificatePojoConverter converter = new CertificatePojoConverter();
        this.id = certificateOrder.getId();
        this.cost = certificateOrder.getCost();

        if (certificateOrder.getOwner() != null) {
            this.owner = new UserPOJO(certificateOrder.getOwner().getId(),
                                      certificateOrder.getOwner().getName(),
                                      certificateOrder.getOwner().getSurname(),
                                      certificateOrder.getOwner().getLogin(),
                                      certificateOrder.getOwner().getPassword(),
                                      certificateOrder.getOwner().getRoles(),
                                      certificateOrder.getOwner().getEmail());
        } else {
            this.owner = null;
        }
        if(certificateOrder.getCertificates() != null){
            this.certificates = converter.convert(certificateOrder.getCertificates());
        }
        this.description = certificateOrder.getDescription();
        this.createdDate = certificateOrder.getCreateTime();
    }

    public CertificateOrderPOJO(String description) {
        this.description = description;
    }
}
