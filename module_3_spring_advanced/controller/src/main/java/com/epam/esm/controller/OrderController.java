package com.epam.esm.controller;

import com.epam.esm.controller.support.ControllerParamNames;
import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.OrderList;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/orders")
public class OrderController {
    private final OrderService service;
    private final DtoConverter<CertificateOrderDTO, CertificateOrderPOJO> orderConverter;

    @Autowired
    public OrderController(OrderService service, DtoConverter<CertificateOrderDTO,
                           CertificateOrderPOJO> orderConverter) {
        this.service = service;
        this.orderConverter = orderConverter;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderList> findAll(
        @RequestParam(
            value = ControllerParamNames.PAGE_PARAM_NAME,
            defaultValue = ControllerParamNames.DEFAULT_PAGE_STRING,
            required = false)
            int page,
        @RequestParam(
            value = ControllerParamNames.SIZE_PARAM_NAME,
            defaultValue = ControllerParamNames.DEFAULT_SIZE_STRING,
            required = false)
            int size) {
        return new ResponseEntity<>(
            new OrderList.OrderListBuilder(service.findAll(page, size), orderConverter)
                .page(page)
                .size(size)
                .resultCount(service.getOrdersCount())
                .build(),
            HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        service.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateOrderDTO>> findOrderById(@PathVariable long id) {
        CertificateOrderDTO searchedOrder = new CertificateOrderDTO(service.find(id));

        return new ResponseEntity<>(searchedOrder.getModel(), HttpStatus.OK);
    }

    @PatchMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateOrderDTO>> addCertificates(
                                                        @PathVariable long id,
                                                        @RequestParam List<Long> certificatesId) {
        CertificateOrderDTO editCertificate =
            new CertificateOrderDTO(service.addCertificates(id, certificatesId));

        return new ResponseEntity<>(editCertificate.getModel(), HttpStatus.OK);
    }
}
