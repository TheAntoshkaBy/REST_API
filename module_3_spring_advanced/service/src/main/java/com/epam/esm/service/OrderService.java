package com.epam.esm.service;

import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.pojo.UserPOJO;

import java.util.List;

public interface OrderService {
    /**
     * This method finds all orders from database using DAO
     **/
    List<CertificateOrderPOJO> findAll(int page, int size);

    /**
     * This method finds concrete Order By Id using DAO
     *
     * @param id Tag Id
     * @return Tag
     **/
    CertificateOrderPOJO find(long id);

    /**
     * This method delete order by transmitted user id
     *
     * @param id user
     **/
    void delete(long id);

    /**
     * This method add new Order
     *
     * @param order User object
     **/
    CertificateOrderPOJO create(CertificateOrderPOJO order, UserPOJO userPOJO);

    /**
     * This method finds all orders from database using DAO
     * which belong concrete user
     **/
    List<CertificateOrderPOJO> findAllByOwner(long id, int offset, int limit);

    List<CertificateOrderPOJO> findAllByOwner(long id);

    int ordersCountByOwner(long id);

    CertificateOrderPOJO addCertificates(long OrderId, List<Long> certificatesId);

    int getOrdersCount();
}
