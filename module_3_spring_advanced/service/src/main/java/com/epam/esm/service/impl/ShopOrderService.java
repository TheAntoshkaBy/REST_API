package com.epam.esm.service.impl;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopOrderService implements OrderService {
    private final OrderRepository repository;

    @Autowired
    public ShopOrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CertificateOrderPOJO> findAll() {
        List<CertificateOrder> certificateOrders = repository.findAll();
        return certificateOrders
                .stream()
                .map(CertificateOrderPOJO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CertificateOrderPOJO find(long id) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public CertificateOrderPOJO create(CertificateOrderPOJO order) {
        return new CertificateOrderPOJO(repository.create(order.pojoToEntity()));
    }//fixme формат получения данных: данные о юзере (айди) и данные о заказе

    @Override
    public List<CertificateOrderPOJO> findAllByOwner(long id) {
        return repository.findAllByOwner(id).stream()
                .map(CertificateOrderPOJO::new)
                .collect(Collectors.toList());
    }
}
