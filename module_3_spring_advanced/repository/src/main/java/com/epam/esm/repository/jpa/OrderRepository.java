package com.epam.esm.repository.jpa;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;

import java.util.List;

public interface OrderRepository {
    void delete(long id);

    CertificateOrder findById(long id);

    List<CertificateOrder> findAll(int offset, int limit);

    CertificateOrder create(CertificateOrder order, User user);

    List<CertificateOrder> findAllByOwner(long id, int offset, int limit);

    List<CertificateOrder> findAllByOwner(long id);

    CertificateOrder addCertificates(CertificateOrder certificateOrder, List<Certificate> certificates, double coast);

    int getOrdersCount();

    int getOrdersCountByOwner(long id);
}
