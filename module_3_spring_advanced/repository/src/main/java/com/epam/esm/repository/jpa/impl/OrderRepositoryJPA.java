package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.constant.EntityNameConstant;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.exception.entity.InvalidDataOutputMessage;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.repository.jpa.ShopJPARepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class OrderRepositoryJPA extends ShopJPARepository<CertificateOrder> implements
    OrderRepository {

    @Override
    public void delete(long id) {

        int col = entityManager.createQuery(SQLRequests.DELETE_ORDER_BY_ID)
            .setParameter(1, id).executeUpdate();
        if (col == 0) {
            throw new RepositoryException(new InvalidDataOutputMessage(EntityNameConstant.ORDER,
                ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }
    }

    @Override
    public CertificateOrder findById(long id) {

        CertificateOrder order = entityManager.find(CertificateOrder.class, id);
        if (order == null) {
            throw new RepositoryException(new InvalidDataOutputMessage(EntityNameConstant.ORDER,
                ErrorTextMessageConstants.NOT_FOUND_ORDER));
        }
        return order;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CertificateOrder> findAll(int offset, int limit) {
        return (List<CertificateOrder>) entityManager
            .createQuery(SQLRequests.FIND_ALL_ORDERS_WITH_LIMIT_OFFSET)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CertificateOrder> findAllByOwner(long id, int offset, int limit) {
        return (List<CertificateOrder>) entityManager
            .createQuery(SQLRequests.FIND_ALL_ORDERS_BY_OWNER)
            .setParameter(1, id)
            .setFirstResult(offset)
            .setMaxResults(limit).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CertificateOrder> findAllByOwner(long id) {
        return (List<CertificateOrder>) entityManager
            .createQuery(SQLRequests.FIND_ALL_ORDERS_BY_OWNER)
            .setParameter(1, id)
            .getResultList();
    }

    @Override
    public CertificateOrder create(CertificateOrder certificateOrder, User user) {
        certificateOrder.setOwner(user);
        entityManager.persist(certificateOrder);
        return certificateOrder;
    }

    @Override
    public CertificateOrder addCertificates(CertificateOrder certificateOrder,
        List<Certificate> certificates,
        BigDecimal cost) {
        certificateOrder.setCertificates(certificates);
        certificateOrder.setCost(cost);
        return certificateOrder;
    }

    @Override
    public int getOrdersCount() {
        Long count = (Long) entityManager.createQuery(SQLRequests.FIND_COUNT_OF_ORDER)
            .getSingleResult();
        return count.intValue();
    }

    @Override
    public int getOrdersCountByOwner(long id) {
        Long count = (Long) entityManager
            .createQuery(SQLRequests.FIND_COUNT_OF_ORDER_BY_OWNER)
            .setParameter(1, id)
            .getSingleResult();
        return count.intValue();
    }
}
