package com.epam.esm.dto;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.support.ControllerSupporter;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderList {
    private CollectionModel<EntityModel<CertificateOrderDTO>> orders;

    private OrderList() {
    }

    public static class OrderListBuilder {
        private final static String NEXT_PAGE_MODEL_PARAM = "next";
        private final static String PREVIOUS_PAGE_MODEL_PARAM = "previous";
        private final static String CURRENT_PAGE_MODEL_PARAM = "current";
        private List<CertificateOrderDTO> ordersDTO;
        private List<CertificateOrderPOJO> ordersPOJO;
        private int ordersCount = 0;
        private int page = 1;
        private int size = 5;
        private CollectionModel<EntityModel<CertificateOrderDTO>> orders;

        public OrderListBuilder(List<CertificateOrderPOJO> orders) {
            this.ordersPOJO = orders;
        }

        public OrderListBuilder page(int page){
            this.page = page;
            return this;
        }

        public OrderListBuilder size(int size){
            this.size = size;
            return this;
        }

        public OrderListBuilder resultCount(int resultCount){
            this.ordersCount = resultCount;
            return this;
        }

        public OrderList build() {
            OrderList orderList = new OrderList();
            CollectionModel<EntityModel<CertificateOrderDTO>>
                ordersListModel = buildModelWithPagination();
            orderList.setOrders(ordersListModel);
            return orderList;
        }

        private CollectionModel<EntityModel<CertificateOrderDTO>> buildModelWithPagination() {
            this.ordersDTO = ControllerSupporter.orderPojoListToOrderDtoList(this.ordersPOJO);

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

            return this.orders;
        }
    }
}
