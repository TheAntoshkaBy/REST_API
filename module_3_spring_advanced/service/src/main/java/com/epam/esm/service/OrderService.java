package com.epam.esm.service;

import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.pojo.UserPOJO;

import java.util.List;

public interface OrderService {
    /**
     * This method finds all orders from database using DAO
     **/
    List<CertificateOrderPOJO> findAll();

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
     * @param tag User object
     **/
    CertificateOrderPOJO create(CertificateOrderPOJO user);
}
