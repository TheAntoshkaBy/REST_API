package com.epam.esm.controller;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.OrderList;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/orders")
public class OrderController {
    private final static String PAGE_NAME_PARAMETER = "page";
    private final static String PAGE_SIZE_NAME_PARAMETER = "size";
    private final static String PAGE_DEFAULT_PARAMETER = "1";
    private final static String PAGE_SIZE_DEFAULT_PARAMETER = "5";
    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(
            @RequestParam(value = PAGE_NAME_PARAMETER,
                    defaultValue = PAGE_DEFAULT_PARAMETER,
                    required = false) int page,
            @RequestParam(value = PAGE_SIZE_NAME_PARAMETER,
                    defaultValue = PAGE_SIZE_DEFAULT_PARAMETER,
                    required = false) int size) {
        return new ResponseEntity<>(new OrderList(
                service.findAll(page, size)
                        .stream()
                        .map(CertificateOrderDTO::new)
                        .collect(Collectors.toList()),
                service.getOrdersCount(),
                page,
                size
        ), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id,
                                         @RequestParam(value = PAGE_NAME_PARAMETER,
                                                 defaultValue = PAGE_DEFAULT_PARAMETER,
                                                 required = false) int page,
                                         @RequestParam(value = PAGE_SIZE_NAME_PARAMETER,
                                                 defaultValue = PAGE_SIZE_DEFAULT_PARAMETER,
                                                 required = false) int size) {
        try {
            service.delete(id);
        } catch (ControllerException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new OrderList(
                service.findAll(page, size)
                        .stream()
                        .map(CertificateOrderDTO::new)
                        .collect(Collectors.toList()),
                service.getOrdersCount(),
                page,
                size
        ), HttpStatus.OK);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findOrderById(@PathVariable long id) {
        int startPage = 1;
        int startSize = 5;

        return new ResponseEntity<>(new CertificateOrderDTO(service.find(id)).getModel(startPage,startSize), HttpStatus.OK);
    }

    @PatchMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCertificates(@PathVariable long id, @RequestParam List<Long> certificatesId) {
        int startPage = 1;
        int startSize = 5;

        return new ResponseEntity<>(new CertificateOrderDTO(service.addCertificates(id, certificatesId))
                .getModel(startPage,startSize), HttpStatus.OK);
    }
}
