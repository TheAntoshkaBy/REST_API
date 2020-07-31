package com.epam.esm.controller;

import com.epam.esm.controller.support.ControllerSupporter;
import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.OrderList;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserList;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.exception.InvalidControllerOutputMessage;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        int startPage = 1;
        int startSize = 5;

        try {
            return new ResponseEntity<>(new CertificateOrderDTO(
                orderService.create(
                    ControllerSupporter.orderDtoToOrderPojo(order),
                    ControllerSupporter
                        .userDtoToUserPojo(new UserDTO(service.find(id)))))
                .getModel(startPage, startSize),
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
        return new ResponseEntity<>(
            new UserList.UserListBuilder(service.findAll(page, size))
                .resultCount(service.getUsersCount())
                .page(page).size(size)
                .build(), HttpStatus.OK);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUserById(@PathVariable long id) {
        int startPage = 1;
        int startSize = 5;

        return new ResponseEntity<>(new UserDTO(service.find(id)).getModel(startPage, startSize),
            HttpStatus.OK);
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

        return new ResponseEntity<>(
            new OrderList.OrderListBuilder(orderService.findAllByOwner(id, page, size))
                .resultCount(orderService.ordersCountByOwner(id))
                .page(page)
                .size(size)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}/orders/{id}")
    public ResponseEntity<Void> deleteOrder(
        @PathVariable Long id,
        @PathVariable Long userId,
        HttpServletRequest request) {
        checkIsCurrentUserHaveRulesForEditThisOrder(userId, id);
        checkUserRulesById(request, userId);

        orderService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "{userId}/orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCertificates(
        @PathVariable long userId,
        @PathVariable long id,
        @RequestParam List<Long> certificatesId,
        HttpServletRequest request) {
        checkIsCurrentUserHaveRulesForEditThisOrder(userId, id);
        checkUserRulesById(request, userId);
        int startPage = 1;
        int startSize = 5;

        return new ResponseEntity<>(new CertificateOrderDTO(
            orderService
                .addCertificates(id, certificatesId)
        )
            .getModel(startPage, startSize), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkIsCurrentUserHaveRulesForEditThisOrder(long userId, long orderId) {
        String exceptionMessageParameter = "user id";
        String exceptionMessage = "You don't have access with action for current order";

        if (!isThisOrderBelowCurrentUser(userId, orderId)) {
            throw new ControllerException(
                new InvalidControllerOutputMessage(exceptionMessageParameter, exceptionMessage)
            );
        }
    }

    private boolean isThisOrderBelowCurrentUser(long userId, long orderId) {
        List<CertificateOrderDTO> orders =
            ControllerSupporter
                .orderPojoListToOrderDtoList(orderService.findAllByOwner(userId));
        return orders.stream()
            .anyMatch(certificateOrderDTO -> certificateOrderDTO.getId() == orderId);
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
