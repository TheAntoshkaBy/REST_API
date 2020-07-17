package com.epam.esm.controller;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.OrderList;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserList;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController()
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final OrderService orderService;

    @Autowired
    public UserController(UserService service, OrderService orderService) {
        this.service = service;
        this.orderService = orderService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@RequestBody UserDTO user) {
        try {
            return new ResponseEntity<>(new UserDTO(service.create(user.dtoToPojo())).getModel(), HttpStatus.CREATED);
        } catch (RepositoryException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "{id}/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addOrder(@PathVariable long id, @RequestBody CertificateOrderDTO order) {
        try {
            return new ResponseEntity<>(new CertificateOrderDTO(
                    orderService.create(order.dtoToPojo(), new UserDTO(service.find(id)).dtoToPojo())).getModel(),
                    HttpStatus.CREATED);
        } catch (RepositoryException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "5") int size) {
        return new ResponseEntity<>(new UserList(
                service.findAll(page, size)
                        .stream()
                        .map(UserDTO::new)
                        .collect(Collectors.toList()),
                service.getUsersCount(),
                page,
                size
        ), HttpStatus.OK);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUserById(@PathVariable long id) {
        return new ResponseEntity<>(new UserDTO(service.find(id)).getModel(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findOrders(@PathVariable long id,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {

        return new ResponseEntity<>(new OrderList(
                orderService.findAllByOwner(id, page, size)
                        .stream()
                        .map(CertificateOrderDTO::new)
                        .collect(Collectors.toList()),
                service.getUsersCount(),
                page,
                size
        ), HttpStatus.OK);
    }

    @DeleteMapping(params = {"page", "size"}, path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "5") int size) {
        try {
            service.delete(id);
        } catch (RepositoryException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new UserList(
                service.findAll(page, size)
                        .stream()
                        .map(UserDTO::new)
                        .collect(Collectors.toList()),
                service.getUsersCount(),
                page,
                size
        ), HttpStatus.OK);
    }
}
