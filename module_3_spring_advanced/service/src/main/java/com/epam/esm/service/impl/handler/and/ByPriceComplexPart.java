package com.epam.esm.service.impl.handler.and;

import org.springframework.stereotype.Component;

@Component
public class ByPriceComplexPart implements ComplexFilter {

    @Override
    public String getPart() {
        return "c.price = :price";
    }

    @Override
    public String getType() {
        return "price";
    }

    @Override
    public Object setType(String param) {
        return Double.parseDouble(param);
    }
}
