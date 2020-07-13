package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.repository.jpa.ShopJPARepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class OrderRepositoryJPA  extends ShopJPARepository<CertificateOrder> implements OrderRepository {
    @Override
    public void delete(long id) {
    }

    @Override
    public CertificateOrder findById(long id) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CertificateOrder> findAll() {
        List<CertificateOrder> certificateOrders = entityManager.createQuery(SQLRequests.FIND_ALL_ORDERS).getResultList();
        return certificateOrders;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CertificateOrder> findAllByOwner(long id) {
        List<CertificateOrder> certificateOrders = entityManager
                .createQuery(SQLRequests.FIND_ALL_ORDERS_BY_OWNER).setParameter(1, id).getResultList();
        return certificateOrders;
    }
}
