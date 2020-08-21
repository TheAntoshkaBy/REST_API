package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.support.ControllerParamNames;
import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderList {

    private CollectionModel<EntityModel<CertificateOrderDTO>> orders;

    private OrderList() {
    }

    public static class OrderListBuilder {

        private final List<CertificateOrderPOJO> ordersPOJO;
        private final DtoConverter<CertificateOrderDTO,CertificateOrderPOJO> converter;
        private int ordersCount = 0;
        private int page = ControllerParamNames.DEFAULT_PAGE;
        private int size = ControllerParamNames.DEFAULT_SIZE;

        public OrderListBuilder(List<CertificateOrderPOJO> orders,
            DtoConverter<CertificateOrderDTO, CertificateOrderPOJO> converter) {
            this.ordersPOJO = orders;
            this.converter = converter;
        }

        public OrderListBuilder page(int page) {
            this.page = page;
            return this;
        }

        public OrderListBuilder size(int size) {
            this.size = size;
            return this;
        }

        public OrderListBuilder resultCount(int resultCount) {
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
            List<CertificateOrderDTO> ordersDTO = converter.convert(this.ordersPOJO);

            CollectionModel<EntityModel<CertificateOrderDTO>> orders = CollectionModel.of(
                ordersDTO
                    .stream()
                    .map(CertificateOrderDTO::getModel)
                    .collect(Collectors.toList())
            );

            if (ordersCount > page * size) {
                int nextPage = page + 1;
                orders.add(linkTo(methodOn(OrderController.class)
                    .findAll(nextPage, size)).withRel(
                        ControllerParamNames.NEXT_PAGE_MODEL_PARAM));
            }

            orders.add(linkTo(methodOn(OrderController.class)
                .findAll(page, size)).withRel(
                    ControllerParamNames.CURRENT_PAGE_MODEL_PARAM));

            if (page != 1) {
                int prevPage = page - 1;
                orders.add(linkTo(methodOn(OrderController.class)
                    .findAll(prevPage, size)).withRel(
                    ControllerParamNames.PREVIOUS_PAGE_MODEL_PARAM));
            }

            int lastPage;
            if(ordersCount>size) {
                lastPage = ordersCount / size;
                if (lastPage % size != 0) {
                    lastPage += 1;
                }
            }else {
                lastPage = 1;
            }
            orders.add(linkTo(methodOn(OrderController.class)
                .findAll(lastPage,size))
                .withRel(ControllerParamNames.LAST_PAGE_MODEL_PARAM));

            return orders;
        }
    }
}
