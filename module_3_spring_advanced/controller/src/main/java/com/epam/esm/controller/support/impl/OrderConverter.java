package com.epam.esm.controller.support.impl;

import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.pojo.CertificateOrderPOJO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements DtoConverter<CertificateOrderDTO, CertificateOrderPOJO> {

    @Override
    public List<CertificateOrderDTO> convert(List<CertificateOrderPOJO> orders) {
        return orders
            .stream()
            .map(CertificateOrderDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    public CertificateOrderPOJO convert(CertificateOrderDTO order) {
        return new CertificateOrderPOJO(
            order.getEndTime(),
            order.getDescription()
        );
    }
}
