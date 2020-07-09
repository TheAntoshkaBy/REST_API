package com.epam.esm.controller;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@RequestBody CertificateOrderDTO order) {
        try {
            service.create(order.dtoToPojo());
            return new ResponseEntity<>(service.findAll()
                    .stream()
                    .map(CertificateOrderDTO::new)
                    .collect(Collectors.toList()), HttpStatus.CREATED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CertificateOrderDTO>> findAll() {
        return new ResponseEntity<>(service.findAll()
                .stream()
                .map(CertificateOrderDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
