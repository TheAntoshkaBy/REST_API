package com.epam.esm.pojo;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.security.acl.Owner;
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

        private UserPOJO owner;

        private List<Certificate> certificates;

        public CertificateOrderPOJO(CertificateOrder certificateOrder){
                this.id = certificateOrder.getId();
                this.coast = certificateOrder.getCoast();

                if(certificateOrder.getOwner() != null){
                        this.owner = new UserPOJO(
                                certificateOrder.getOwner().getId(),
                                certificateOrder.getOwner().getName(),
                                certificateOrder.getOwner().getSurname(),
                                certificateOrder.getOwner().getLogin()
                        );
                }else {
                        this.owner = null;
                }
                this.timestamp = certificateOrder.getTimestamp();
                this.certificates = certificateOrder.getCertificates();
        }

        public CertificateOrder pojoToEntity(){
                return new CertificateOrder(
                        this.timestamp,
                        this.coast
                );
        }

        public CertificateOrderPOJO(Date timestamp, Double coast) {
                this.timestamp = timestamp;
                this.coast = coast;
        }
}
