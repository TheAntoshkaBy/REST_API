package com.epam.esm.service;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.repository.jpa.impl.CertificateRepositoryJPA;
import com.epam.esm.repository.jpa.impl.OrderRepositoryJPA;
import com.epam.esm.service.impl.ShopOrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ShopOrderServiceTest {
    private OrderRepository repository;
    private CertificateRepository certificateRepository;
    private ShopOrderService orderService;

    private CertificateOrder order;
    private List<CertificateOrder> orders;

    @Before
    public void init() {
        repository = mock(OrderRepositoryJPA.class);
        certificateRepository = mock(CertificateRepositoryJPA.class);
        orderService = new ShopOrderService(repository,certificateRepository);

        order = new CertificateOrder(new Date(),321.23,"dd",new Date());
        CertificateOrder order2 = new CertificateOrder(new Date(),321.23,"dd",new Date());
        orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);
    }

    @Test
    public void findAll() {
        when(repository.findAll(anyInt(),anyInt())).thenReturn(orders);

        List<CertificateOrderPOJO> userActual = orderService.findAll(1,5);
        Assert.assertEquals(userActual.stream().map(CertificateOrderPOJO::pojoToEntity)
                .collect(Collectors.toList()), orders);
    }

    @Test
    public void find()  {
        Long getFirst = 1l;
        order = orders.get(getFirst.intValue());
        when(repository.findById(anyInt())).thenReturn(orders.get(0));

        Assert.assertEquals(order, orders.get(getFirst.intValue()));
    }

    @Test
    public void delete() {
        Long tagId = 2L;
        Long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(getFirst.intValue());
            assertEquals(tagId.intValue(), id);
            orders.remove(tagId.intValue());
            return null;
        }).when(repository).findById(anyInt());

        List<CertificateOrder> expectedTags = orders;

        orderService.delete(tagId);

        assertEquals(expectedTags, orders);
    }

    @Test
    public void create() {
        Long tagId = 2L;
        Long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(getFirst.intValue());
            assertEquals(tagId.intValue(), id);
            orders.add(order);
            return null;
        }).when(repository).create(any(CertificateOrder.class), any(User.class));

        List<CertificateOrder> expectedTags = orders;

        assertEquals(expectedTags, orders);
    }
}
