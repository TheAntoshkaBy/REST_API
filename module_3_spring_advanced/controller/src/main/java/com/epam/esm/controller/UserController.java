package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.exception.InvalidControllerOutputMessage;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
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
    private final static String PAGE_NAME_PARAMETER = "page";
    private final static String PAGE_SIZE_NAME_PARAMETER = "size";
    private final static String PAGE_DEFAULT_PARAMETER = "1";
    private final static String PAGE_SIZE_DEFAULT_PARAMETER = "5";
    private final UserService service;
    private final OrderService orderService;
    private final TagService tagService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(
            UserService service,
            OrderService orderService,
            TagService tagService,
            JwtTokenProvider jwtTokenProvider) {
        this.service = service;
        this.orderService = orderService;
        this.tagService = tagService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PatchMapping(path = "{id}/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addOrder(
            @PathVariable long id,
            @Valid @RequestBody CertificateOrderDTO order,
            HttpServletRequest request) {
        checkUserRulesById(request, id);

        try {
            return new ResponseEntity<>(new CertificateOrderDTO(
                    orderService.create(
                            order.dtoToPojo(),
                            new UserDTO(service.find(id)).dtoToPojo())).getModel(),
                    HttpStatus.CREATED);
        } catch (ControllerException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(
            @RequestParam(value = PAGE_NAME_PARAMETER,
                    defaultValue = PAGE_DEFAULT_PARAMETER) int page,
            @RequestParam(value = PAGE_SIZE_NAME_PARAMETER,
                    defaultValue = PAGE_SIZE_DEFAULT_PARAMETER) int size) {
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

    @GetMapping(path = "/orders/tags", params = "search by", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findMostWidelyUsedTagByMostActiveUser() {
        return new ResponseEntity<>(new TagDTO(tagService.findMostWidelyUsedTag()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findOrders(@PathVariable long id,
                                        @RequestParam(value = PAGE_NAME_PARAMETER,
                                                defaultValue = PAGE_DEFAULT_PARAMETER) int page,
                                        @RequestParam(value = PAGE_SIZE_NAME_PARAMETER,
                                                defaultValue = PAGE_SIZE_DEFAULT_PARAMETER) int size) {

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
    public ResponseEntity<?> deleteOrder(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestParam(value = PAGE_NAME_PARAMETER,
                    defaultValue = PAGE_DEFAULT_PARAMETER,
                    required = false) int page,
            @RequestParam(value = PAGE_SIZE_NAME_PARAMETER,
                    defaultValue = PAGE_SIZE_DEFAULT_PARAMETER,
                    required = false) int size,
            HttpServletRequest request) {
        checkIsCurrentUserHaveRulesForEditThisOrder(userId, id);
        checkUserRulesById(request, userId);

        List<CertificateOrderDTO> orders;
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
        checkIsCurrentUserHaveRulesForEditThisOrder(userId, id);
        checkUserRulesById(request, userId);

        return new ResponseEntity<>(new CertificateOrderDTO(
                orderService
                        .addCertificates(id, certificatesId)
        )
                .getModel(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
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

    void checkIsCurrentUserHaveRulesForEditThisOrder(long userId, long orderId) {
        String exceptionMessageParameter = "user id";
        String exceptionMessage = "You don't have access with action for current order";

        if (!isThisOrderBelowCurrentUser(userId, orderId)) {
            throw new ControllerException(
                    new InvalidControllerOutputMessage(exceptionMessageParameter, exceptionMessage)
            );
        }
    }

    boolean isThisOrderBelowCurrentUser(long userId, long orderId) {
        List<CertificateOrderDTO> orders = orderService.findAllByOwner(userId)
                .stream()
                .map(CertificateOrderDTO::new)
                .collect(Collectors.toList());
        return orders.stream().anyMatch(certificateOrderDTO -> certificateOrderDTO.getId() == orderId);
    }

    private void checkUserRulesById(HttpServletRequest req, long actionUserId) {
        String exceptionMessageParameter = "user id";
        String exceptionMessage = "You don't have access with action for current user";

        String token = jwtTokenProvider.resolveToken(req);
        String username = jwtTokenProvider.getUsername(token);
        UserDTO userDTO = new UserDTO(service.findByLogin(username));

        if (userDTO.getId() != actionUserId && userDTO.getRoles().stream()
                .noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new ControllerException(
                    new InvalidControllerOutputMessage(exceptionMessageParameter, exceptionMessage)
            );
        }
    }
}
