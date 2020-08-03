package com.epam.esm.controller;

import com.epam.esm.controller.support.ControllerSupporter;
import com.epam.esm.controller.support.OrderSupporter;
import com.epam.esm.controller.support.UserSupporter;
import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.OrderList;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserList;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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

    private UserService service;
    private OrderService orderService;
    private TagService tagService;
    private JwtTokenProvider jwtTokenProvider;

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
    public ResponseEntity<EntityModel<CertificateOrderDTO>> addOrder(
        @PathVariable long id,
        @Valid @RequestBody CertificateOrderDTO order,
        HttpServletRequest request) {
        UserSupporter.checkUserRulesById(request, id);

        return new ResponseEntity<>(new CertificateOrderDTO(
            orderService.create(
                OrderSupporter.orderDtoToOrderPojo(order),
                UserSupporter
                    .userDtoToUserPojo(new UserDTO(service.find(id)))))
            .getModel(),
            HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserList> findAll(
        @RequestParam(value = ControllerSupporter.PAGE_PARAM_NAME,
            defaultValue = ControllerSupporter.DEFAULT_PAGE_STRING) int page,
        @RequestParam(value = ControllerSupporter.SIZE_PARAM_NAME,
            defaultValue = ControllerSupporter.DEFAULT_SIZE_STRING) int size) {
        return new ResponseEntity<>(
            new UserList.UserListBuilder(service.findAll(page, size))
                .resultCount(service.getUsersCount())
                .page(page).size(size)
                .build(), HttpStatus.OK);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<UserDTO>> findUserById(@PathVariable long id) {
        int startPage = 1;
        int startSize = 5;

        return new ResponseEntity<>(new UserDTO(service.find(id)).getModel(startPage, startSize),
            HttpStatus.OK);
    }

    @GetMapping(path = "/orders/tags",
        params = "search by",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDTO> findMostWidelyUsedTagByMostActiveUser() {
        return new ResponseEntity<>(new TagDTO(tagService.findMostWidelyUsedTag()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderList> findOrders(@PathVariable long id,
        @RequestParam(value = ControllerSupporter.PAGE_PARAM_NAME,
            defaultValue = ControllerSupporter.DEFAULT_PAGE_STRING) int page,
        @RequestParam(value = ControllerSupporter.SIZE_PARAM_NAME,
            defaultValue = ControllerSupporter.DEFAULT_SIZE_STRING) int size) {

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
        UserSupporter.checkIsCurrentUserHaveRulesForEditThisOrder(userId, id);
        UserSupporter.checkUserRulesById(request, userId);

        orderService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "{userId}/orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<CertificateOrderDTO>> addCertificates(
        @PathVariable long userId,
        @PathVariable long id,
        @RequestParam List<Long> certificatesId,
        HttpServletRequest request) {
        UserSupporter.checkIsCurrentUserHaveRulesForEditThisOrder(userId, id);
        UserSupporter.checkUserRulesById(request, userId);

        return new ResponseEntity<>(new CertificateOrderDTO(
            orderService
                .addCertificates(id, certificatesId)
        )
            .getModel(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
