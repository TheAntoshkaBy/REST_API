package com.epam.esm.controller;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.OrderList;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserList;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.exception.InvalidControllerOutputMessage;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
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

    @PatchMapping(path = "{id}/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addOrder(@PathVariable long id, @Valid @RequestBody CertificateOrderDTO order) {
        try {
            return new ResponseEntity<>(new CertificateOrderDTO(
                    orderService.create(order.dtoToPojo(), new UserDTO(service.find(id)).dtoToPojo())).getModel(),
                    HttpStatus.CREATED);
        } catch (ControllerException e) {
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

        List<CertificateOrderDTO> orders = orderService.findAllByOwner(id, page, size)
                .stream()
                .map(CertificateOrderDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new OrderList(
                orders,
                orderService.ordersCountByOwner(id),
                page,
                size
        ), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id,
                                         @PathVariable Long userId,
                                         @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                         @RequestParam(value = "size", defaultValue = "5", required = false) int size) {

        String invalid = "this parameter is invalid for this user!";
        List<CertificateOrderDTO> orders = orderService.findAllByOwner(userId, page, size)
                .stream()
                .map(CertificateOrderDTO::new)
                .collect(Collectors.toList());
        if (orders.stream().noneMatch(certificateOrderDTO -> certificateOrderDTO.getId().equals(id))) {
            throw new ControllerException(new InvalidControllerOutputMessage("id", invalid));
        }
        try {
            orderService.delete(id);
            orders = orderService.findAllByOwner(userId, page, size)
                    .stream()
                    .map(CertificateOrderDTO::new)
                    .collect(Collectors.toList());
        } catch (ControllerException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new OrderList(
                orders,
                orderService.ordersCountByOwner(userId),
                page,
                size
        ), HttpStatus.OK);
    }

    @PatchMapping(path = "{userId}/orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCertificates(
            @PathVariable long userId,
            @PathVariable long id,
            @RequestParam List<Long> certificatesId,
            HttpServletRequest request) {
        System.out.println(request.getHeader("auth"));
        return new ResponseEntity<>(new CertificateOrderDTO(orderService.addCertificates(id, certificatesId)).getModel()
                , HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                    @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        try {
            service.delete(id);
        } catch (ControllerException e) {
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
