package com.epam.esm.service.support.impl;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.service.support.PojoConverter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderPojoConverter implements PojoConverter<CertificateOrderPOJO, CertificateOrder> {

    @Override
    public List<CertificateOrderPOJO> convert(List<CertificateOrder> orders) {
        return orders
            .stream()
            .map(CertificateOrderPOJO::new)
            .collect(Collectors.toList());
    }

    @Override
    public CertificateOrder convert(CertificateOrderPOJO order) {
        return new CertificateOrder(order.getEndDate(), order.getCost(), order.getDescription(),
                                    order.getCreatedDate()
        );
    }
}
