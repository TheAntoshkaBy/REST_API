package com.epam.esm.controller;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.OrderList;
import com.epam.esm.exception.RepositoryException;
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
    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping(params = {"page", "size"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "5") int size) {
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

    @DeleteMapping(params = {"page", "size"}, path = "/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id,
                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "size", defaultValue = "5") int size) {
        try {
            service.delete(id);
        } catch (RepositoryException e) {
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
        return new ResponseEntity<>(new CertificateOrderDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @PatchMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCertificates(@PathVariable long id, @RequestParam List<Long> certificatesId) {
        return new ResponseEntity<>(new CertificateOrderDTO(service.addCertificates(id, certificatesId)).getModel()
                , HttpStatus.OK);
    }
}
