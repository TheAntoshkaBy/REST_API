package com.epam.esm.service.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopOrderService implements OrderService {
    private final OrderRepository repository;
    private final CertificateRepository certificateRepository;

    @Autowired
    public ShopOrderService(OrderRepository repository, CertificateRepository certificateRepository) {
        this.repository = repository;
        this.certificateRepository = certificateRepository;
    }

    @Override
    public List<CertificateOrderPOJO> findAll(int page, int size) {
        if (page != 1) {
            page = size * (page - 1) + 1;
        }
        List<CertificateOrder> certificateOrders = repository.findAll(--page, size);
        return certificateOrders
                .stream()
                .map(CertificateOrderPOJO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CertificateOrderPOJO find(long id) {
        return new CertificateOrderPOJO(repository.findById(id));
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    @Override
    public CertificateOrderPOJO create(CertificateOrderPOJO order, UserPOJO userPOJO) {
        order.setCoast(0.0);
        order.setCreatedDate(new Date());
        return new CertificateOrderPOJO(repository.create(order.pojoToEntity(), userPOJO.pojoToEntity()));
    }

    @Override
    public List<CertificateOrderPOJO> findAllByOwner(long id, int page, int size) {
        if (page != 1) {
            page = size * (page - 1) + 1;
        }
        return repository.findAllByOwner(id, --page, size).stream()
                .map(CertificateOrderPOJO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificateOrderPOJO> findAllByOwner(long id) {
        return repository.findAllByOwner(id).stream()
                .map(CertificateOrderPOJO::new)
                .collect(Collectors.toList());
    }

    @Override
    public int ordersCountByOwner(long id) {
        return repository.getOrdersCountByOwner(id);
    }

    @Override
    public CertificateOrderPOJO addCertificates(long orderId, List<Long> certificatesId) {
        CertificateOrder certificateOrder = repository.findById(orderId);
        List<Certificate> certificates = certificatesId
                .stream()
                .map(certificateRepository::findById)
                .collect(Collectors.toList());
        double summaryPrice = certificates.stream().mapToDouble(Certificate::getPrice).sum();
        return new CertificateOrderPOJO(repository.addCertificates(certificateOrder, certificates, summaryPrice));
    }

    @Override
    public int getOrdersCount() {
        return repository.getOrdersCount();
    }
}
