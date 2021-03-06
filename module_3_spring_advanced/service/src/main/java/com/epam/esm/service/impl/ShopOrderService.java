package com.epam.esm.service.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.User;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.support.PojoConverter;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.OrderValidator;
import com.epam.esm.service.validator.UserValidator;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ShopOrderService implements OrderService {

    private final OrderRepository repository;
    private final CertificateRepository certificateRepository;
    private final PojoConverter<CertificateOrderPOJO, CertificateOrder> converter;
    private final PojoConverter<UserPOJO, User> userConverter;
    private final OrderValidator validator;
    private final UserValidator userValidator;
    private final CertificateValidator certificateValidator;

    @Autowired
    public ShopOrderService(OrderRepository repository, CertificateRepository certificateRepository,
        PojoConverter<CertificateOrderPOJO, CertificateOrder> converter,
        PojoConverter<UserPOJO, User> userConverter,
        OrderValidator validator, UserValidator userValidator,
        CertificateValidator certificateValidator) {
        this.repository = repository;
        this.certificateRepository = certificateRepository;
        this.converter = converter;
        this.userConverter = userConverter;
        this.validator = validator;
        this.userValidator = userValidator;
        this.certificateValidator = certificateValidator;
    }

    @Override
    public List<CertificateOrderPOJO> findAll(int page, int size) {
        page = PojoConverter.convertPaginationPageToDbOffsetParameter(page, size);
        List<CertificateOrder> certificateOrders = repository.findAll(--page, size);
        return converter.convert(certificateOrders);
    }

    @Override
    public CertificateOrderPOJO find(long id) {
        validator.checkId(id);
        return new CertificateOrderPOJO(repository.findById(id));
    }

    @Override
    public void delete(long id) {
        validator.checkId(id);
        repository.delete(id);
    }

    @Override
    public CertificateOrderPOJO create(CertificateOrderPOJO order, UserPOJO userPOJO) {
        order.setCost(new BigDecimal(0));
        order.setCreatedDate(new Date());

        return new CertificateOrderPOJO(
            repository.create(converter.convert(order), userConverter.convert(userPOJO)));
    }

    @Override
    public List<CertificateOrderPOJO> findAllByOwner(long id, int page, int size) {
        page = PojoConverter.convertPaginationPageToDbOffsetParameter(page, size);
        userValidator.checkId(id);
        return converter.convert(repository.findAllByOwner(id, --page, size));
    }

    @Override
    public List<CertificateOrderPOJO> findAllByOwner(long id) {
        userValidator.checkId(id);
        return converter.convert(repository.findAllByOwner(id));
    }

    @Override
    public int ordersCountByOwner(long id) {
        userValidator.checkId(id);
        return repository.getOrdersCountByOwner(id);
    }

    @Override
    public CertificateOrderPOJO addCertificates(long orderId, List<Long> certificatesId) {
        CertificateOrder certificateOrder = repository.findById(orderId);
        certificatesId.forEach(certificateValidator::checkId);
        List<Certificate> certificates = certificatesId
            .stream()
            .map(certificateRepository::findById)
            .collect(Collectors.toList());
        BigDecimal summaryPrice = BigDecimal.valueOf(0);
        for (Certificate certificate : certificates) {
            summaryPrice = summaryPrice.add(certificate.getPrice());
        }

        return new CertificateOrderPOJO(
            repository.addCertificates(certificateOrder, certificates, summaryPrice));
    }

    @Override
    public int getOrdersCount() {
        return repository.getOrdersCount();
    }
}
