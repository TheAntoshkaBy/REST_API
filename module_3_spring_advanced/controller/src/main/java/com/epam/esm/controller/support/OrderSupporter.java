package com.epam.esm.controller.support;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.pojo.CertificateOrderPOJO;
import java.util.List;
import java.util.stream.Collectors;

public class OrderSupporter {

    public static final String ERROR_END_TIME = "End time must be null";
    public static final String ERROR_ID = "Id must be null";
    public static final String ERROR_COST = "Cost must be null";
    public static final String ERROR_CREATED_TIME = "Created time must be null";
    public static final String ERROR_OWNER = "Owner must be not null";
    public static final String ERROR_CERTIFICATES = "Certificates must be null";
    public static final String ERROR_DESCRIPTION = "Description must be between "
        + "3 and 170 characters";

    public static List<CertificateOrderDTO> orderPojoListToOrderDtoList(
        List<CertificateOrderPOJO> orders) {
        return orders
            .stream()
            .map(CertificateOrderDTO::new)
            .collect(Collectors.toList());
    }

    public static CertificateOrderPOJO orderDtoToOrderPojo(CertificateOrderDTO order) {
        return new CertificateOrderPOJO(
            order.getEndTime(),
            order.getDescription()
        );
    }
}
