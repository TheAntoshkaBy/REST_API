package com.epam.esm.service.support;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.Tag;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceSupporter {

    public static int setCurrentOffsetFromPageToDb(int page, int size){
        if (page != 1) {
            return size * (page - 1) + 1;
        }else {
            return page;
        }
    }

    public static List<CertificatePOJO> certificateEntityToCertificatePOJO(List<Certificate> certificates){
       return certificates.stream()
               .map(CertificatePOJO::new)
               .collect(Collectors.toList());
   }

    public static List<CertificateOrderPOJO> orderEntityToOrderCertificatePOJO(List<CertificateOrder> orders){
        return orders.stream()
                .map(CertificateOrderPOJO::new)
                .collect(Collectors.toList());
    }

    public static List<TagPOJO> tagEntityToTagPOJO(List<Tag> tags){
        return tags.stream()
                .map(TagPOJO::new)
                .collect(Collectors.toList());
    }
}
