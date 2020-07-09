package com.epam.esm.pojo;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
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
        private Date timestamp;
        private Double coast;
        private User owner;

        private List<Certificate> certificates;

        public CertificateOrderPOJO(CertificateOrder certificateOrder){
                this.id = certificateOrder.getId();
                this.coast = certificateOrder.getCoast();
                this.owner = certificateOrder.getOwner();
                this.timestamp = certificateOrder.getTimestamp();
                this.certificates = certificateOrder.getCertificates();
        }

        public CertificateOrder pojoToEntity(){
                return new CertificateOrder(
                        this.id,
                        this.timestamp,
                        this.coast,
                        this.owner,
                        this.certificates
                );
        }
}
