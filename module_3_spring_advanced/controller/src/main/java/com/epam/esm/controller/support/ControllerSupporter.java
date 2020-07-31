package com.epam.esm.controller.support;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificateList;
import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.RegistrationUserDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.pojo.CertificatePOJO;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.pojo.UserPOJO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerSupporter {

    public static List<CertificateDTO> certificatePojoListToCertificateDtoList(
        List<CertificatePOJO> certificates) {
        return certificates
            .stream()
            .map(CertificateDTO::new)
            .collect(Collectors.toList());
    }

    public static List<CertificateOrderDTO> orderPojoListToOrderDtoList(
        List<CertificateOrderPOJO> orders) {
        return orders
            .stream()
            .map(CertificateOrderDTO::new)
            .collect(Collectors.toList());
    }

    public static List<TagDTO> tagPojoListToTagDtoList(List<TagPOJO> tags) {
        return tags
            .stream()
            .map(TagDTO::new)
            .collect(Collectors.toList());
    }

    public static List<UserDTO> userPojoListToUserDtoList(List<UserPOJO> users) {
        return users
            .stream()
            .map(UserDTO::new)
            .collect(Collectors.toList());
    }

    public static CertificatePOJO certificateDtoToCertificatePOJO(CertificateDTO certificate) {
        return new CertificatePOJO(
            certificate.getName(),
            certificate.getDescription(),
            certificate.getPrice(),
            certificate.getDurationDays()
        );
    }

    public static CertificateOrderPOJO orderDtoToOrderPojo(CertificateOrderDTO order) {
        return new CertificateOrderPOJO(
            order.getEndTime(),
            order.getDescription()
        );
    }

    public static TagPOJO tagDtoToTagPOJO(TagDTO tag) {
        return new TagPOJO(tag.getId(), tag.getName());
    }

    public static UserPOJO userDtoToUserPojo(UserDTO user) {
        return new UserPOJO(
            user.getId(),
            user.getName(),
            user.getSurname(),
            user.getLogin(),
            user.getPassword(),
            user.getRoles(),
            user.getEmail()
        );
    }

    public static UserPOJO userRegistrationDtoToUserPojo(RegistrationUserDTO user) {
        return new UserPOJO(
            user.getName(),
            user.getSurname(),
            user.getLogin(),
            user.getPassword(),
            user.getRoles(),
            user.getEmail()
        );
    }

    public static CertificateList formationCertificateList(
        Map<List<CertificatePOJO>, Integer> certificatesMap,
        int page,
        int size,
        Map<String, String> params) {
        List<CertificatePOJO> certificates = certificatesMap
            .entrySet()
            .iterator()
            .next()
            .getKey();
        int resultCount = certificatesMap.get(certificates);
        List<CertificateDTO> certificatesDTO = ControllerSupporter
            .certificatePojoListToCertificateDtoList(certificates);

        return new CertificateList
            .CertificateListBuilder(certificatesDTO)
            .page(page)
            .size(size)
            .parameters(params)
            .resultCount(resultCount)
            .build();
    }

    public static int getValidPaginationParam(String page, String paramName) {
        int defaultPage = 1;
        int defaultSize = 5;

        int defaultReturn = paramName.equals("page") ? defaultSize : defaultPage;
        try {
            int paramInteger = Integer.parseInt(page);
            return paramInteger > 0 ? paramInteger : defaultReturn;
        } catch (NumberFormatException e) {
            return defaultReturn;
        }
    }
}
