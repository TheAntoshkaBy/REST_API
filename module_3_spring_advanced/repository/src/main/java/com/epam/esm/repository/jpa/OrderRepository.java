package com.epam.esm.repository.jpa;

import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;

import java.util.List;

public interface OrderRepository {
    void delete(long id);

    CertificateOrder findById(long id);

    List<CertificateOrder> findAll();

    CertificateOrder create(CertificateOrder order);

    List<CertificateOrder> findAllByOwner(long id);
}
