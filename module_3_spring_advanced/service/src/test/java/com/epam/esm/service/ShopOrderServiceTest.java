package com.epam.esm.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.repository.jpa.impl.CertificateRepositoryJPA;
import com.epam.esm.repository.jpa.impl.OrderRepositoryJPA;
import com.epam.esm.service.impl.ShopOrderService;
import com.epam.esm.service.support.impl.OrderPojoConverter;
import com.epam.esm.service.support.impl.UserPojoConverter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class ShopOrderServiceTest {

    private OrderRepository repository;
    private CertificateRepository certificateRepository;
    private ShopOrderService orderService;
    private OrderPojoConverter converter;
    private UserPojoConverter userConverter;

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 5;

    private CertificateOrder order;
    private List<CertificateOrder> orders;

    @Before
    public void init() {
        repository = mock(OrderRepositoryJPA.class);
        certificateRepository = mock(CertificateRepositoryJPA.class);
        converter = mock(OrderPojoConverter.class);
        userConverter = mock(UserPojoConverter.class);
        orderService = new ShopOrderService(repository, certificateRepository, converter,
                                            userConverter);

        order = new CertificateOrder(new Date(), BigDecimal.valueOf(321.23),
                           "dd", new Date());
        CertificateOrder order2 = new CertificateOrder(new Date(), BigDecimal.valueOf(321.23),
                                            "dd", new Date());
        orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);
    }

    @Test
    public void findAll() {
        when(repository.findAll(anyInt(), anyInt())).thenReturn(orders);
        when(converter.convert(anyList())).thenReturn(orders.stream()
            .map(CertificateOrderPOJO::new)
            .collect(Collectors.toList()));

        List<CertificateOrderPOJO> userActual = orderService.findAll(anyInt(), anyInt());
        assertEquals(userActual,orders.stream()
                                      .map(CertificateOrderPOJO::new)
                                      .collect(Collectors.toList()));
    }

    @Test
    public void find() {
        long getFirst = 1l;

        order = orders.get((int) getFirst);
        when(repository.findById(anyInt())).thenReturn(orders.get(0));

        assertEquals(order, orders.get((int) getFirst));
    }

    @Test
    public void delete() {
        long tagId = 2L;
        long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument((int) getFirst);
            assertEquals((int) tagId, id);
            orders.remove((int) tagId);
            return null;
        }).when(repository).findById(anyInt());

        List<CertificateOrder> expectedTags = orders;

        orderService.delete(tagId);

        assertEquals(expectedTags, orders);
    }

    @Test
    public void create() {
        long tagId = 2L;
        long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument((int) getFirst);
            assertEquals((int) tagId, id);
            orders.add(order);
            return null;
        }).when(repository).create(any(CertificateOrder.class), any(User.class));

        List<CertificateOrder> expectedTags = orders;

        assertEquals(expectedTags, orders);
    }

    @Test
    public void ordersByOwner_OwnerId_OwnerOrders() {
        int id = 0;

        when(repository.findAllByOwner(id)).thenReturn(orders);
        when(converter.convert(orders)).thenReturn(orders
            .stream()
            .map(CertificateOrderPOJO::new).collect(Collectors.toList()));

        List<CertificateOrderPOJO> expected = orderService.findAllByOwner(id);
        assertEquals(expected, orders.stream()
                                     .map(CertificateOrderPOJO::new)
                                     .collect(Collectors.toList()));
    }

    @Test
    public void ordersByOwnerWithPagination_OwnerId_OwnerOrdersWithPagination() {
        int id = 0;

        orders.clear();
        when(repository.findAllByOwner(id, DEFAULT_PAGE, DEFAULT_SIZE)).thenReturn(orders);
        when(converter.convert(orders)).thenReturn(orders
            .stream()
            .map(CertificateOrderPOJO::new).collect(Collectors.toList()));

        List<CertificateOrderPOJO> expected = orderService
            .findAllByOwner(anyInt(), anyInt(), anyInt());

        assertEquals(expected, orders.stream()
            .map(CertificateOrderPOJO::new).collect(Collectors.toList()));
    }

    @Test
    public void ordersCountByOwnerOwnerId_OwnerOrdersCount() {
        int id = 0;
        int count = 1;

        when(repository.getOrdersCountByOwner(id)).thenReturn(count);

        int expected = orderService.ordersCountByOwner(id);

        assertEquals(expected, count);
    }

    @Test
    public void getOrdersCount_WithoutParams_GetCountOfOrders() {
        int count = 1;

        when(repository.getOrdersCount()).thenReturn(count);

        int expected = orderService.getOrdersCount();

        assertEquals(expected, count);
    }
}
