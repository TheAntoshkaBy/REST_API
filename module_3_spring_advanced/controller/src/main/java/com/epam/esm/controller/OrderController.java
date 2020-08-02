package com.epam.esm.controller;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.OrderList;
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

    private static final String PAGE_NAME_PARAMETER = "page";
    private static final String PAGE_SIZE_NAME_PARAMETER = "size";
    private static final String PAGE_DEFAULT_PARAMETER = "1";
    private static final String PAGE_SIZE_DEFAULT_PARAMETER = "5";
    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderList> findAll(
        @RequestParam(
            value = PAGE_NAME_PARAMETER,
            defaultValue = PAGE_DEFAULT_PARAMETER,
            required = false)
            int page,
        @RequestParam(
            value = PAGE_SIZE_NAME_PARAMETER,
            defaultValue = PAGE_SIZE_DEFAULT_PARAMETER,
            required = false)
            int size) {
        return new ResponseEntity<>(
            new OrderList.OrderListBuilder(service.findAll(page, size))
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

        return new ResponseEntity<>(
            new CertificateOrderDTO(service.find(id)).getModel(),
            HttpStatus.OK);
    }

    @PatchMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateOrderDTO>> addCertificates(
        @PathVariable long id, @RequestParam List<Long> certificatesId) {

        return new ResponseEntity<>(
            new CertificateOrderDTO(service.addCertificates(id, certificatesId))
                .getModel(),
            HttpStatus.OK);
    }
}
