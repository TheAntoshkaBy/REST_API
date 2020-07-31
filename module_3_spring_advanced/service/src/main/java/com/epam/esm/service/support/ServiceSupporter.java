package com.epam.esm.service.support;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateOrder;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.pojo.UserPOJO;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceSupporter {

    public static int convertPaginationPageToDbOffsetParameter(int page, int size) {
        if (page != 1) {
            return size * (page - 1) + 1;
        } else {
            return page;
        }
    }

    public static List<CertificatePOJO> convertCertificateEntityToCertificatePOJO(
        List<Certificate> certificates) {
        return certificates.stream()
            .map(CertificatePOJO::new)
            .collect(Collectors.toList());
    }

    public static List<CertificateOrderPOJO> convertOrderEntityToOrderCertificatePOJO(
        List<CertificateOrder> orders) {
        return orders.stream()
            .map(CertificateOrderPOJO::new)
            .collect(Collectors.toList());
    }

    public static List<TagPOJO> convertTagEntityToTagPOJO(List<Tag> tags) {
        return tags.stream()
            .map(TagPOJO::new)
            .collect(Collectors.toList());
    }

    public static Certificate convertCertificatePojoToCertificate(CertificatePOJO certificate) {
        if (certificate.getId() == null) {
            return new Certificate(
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDurationDays()
            );
        } else {
            return new Certificate(
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDurationDays()
            );
        }
    }

    public static CertificateOrder convertOrderPojoToOrder(CertificateOrderPOJO order) {
        return new CertificateOrder(
            order.getEndDate(),
            order.getCost(),
            order.getDescription(),
            order.getCreatedDate()
        );
    }

    public static Tag convertTagPojoToTag(TagPOJO tag) {
        return new Tag(tag.getId(), tag.getName());
    }

    public static User convertUserPojoToUserEntity(UserPOJO userPOJO) {
        if (userPOJO.getId() == null) {
            return new User(
                userPOJO.getName(),
                userPOJO.getSurname(),
                userPOJO.getLogin(),
                userPOJO.getPassword(),
                userPOJO.getRoles(),
                userPOJO.getEmail()
            );
        } else {
            return new User(
                userPOJO.getId(),
                userPOJO.getName(),
                userPOJO.getSurname(),
                userPOJO.getLogin(),
                userPOJO.getPassword(),
                userPOJO.getRoles(),
                userPOJO.getEmail()
            );
        }
    }
}
