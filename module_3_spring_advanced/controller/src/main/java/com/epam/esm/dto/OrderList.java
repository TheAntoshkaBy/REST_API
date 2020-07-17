package com.epam.esm.dto;

import com.epam.esm.controller.OrderController;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderList {
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
                    .findAll(nextPage, size)).withRel("next"));
        }

        this.orders.add(linkTo(methodOn(OrderController.class)
                .findAll(page, size)).withRel("current"));

        if (page != 1) {
            int prevPage = page - 1;
            this.orders.add(linkTo(methodOn(OrderController.class)
                    .findAll(prevPage, size)).withRel("previous"));
        }
    }

}
