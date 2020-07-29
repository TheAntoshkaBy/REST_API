package com.epam.esm.dto;

import com.epam.esm.controller.OrderController;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderList {

    @JsonIgnore
    private final static String NEXT_PAGE_MODEL_PARAM = "next";

    @JsonIgnore
    private final static String PREVIOUS_PAGE_MODEL_PARAM = "previous";

    @JsonIgnore
    private final static String CURRENT_PAGE_MODEL_PARAM = "current";

    private CollectionModel<EntityModel<CertificateOrderDTO>> orders;

    public OrderList(List<CertificateOrderDTO> ordersDTO, int ordersCount, int page, int size) {
        this.orders = CollectionModel.of(
                ordersDTO
                        .stream()
                        .map(CertificateOrderDTO::getModel)
                        .collect(Collectors.toList())
        );

        if (ordersCount > page * size) {
            int nextPage = page + 1;
            this.orders.add(linkTo(methodOn(OrderController.class)
                    .findAll(nextPage, size)).withRel(NEXT_PAGE_MODEL_PARAM));
        }

        this.orders.add(linkTo(methodOn(OrderController.class)
                .findAll(page, size)).withRel(CURRENT_PAGE_MODEL_PARAM));

        if (page != 1) {
            int prevPage = page - 1;
            this.orders.add(linkTo(methodOn(OrderController.class)
                    .findAll(prevPage, size)).withRel(PREVIOUS_PAGE_MODEL_PARAM));
        }
    }
}
