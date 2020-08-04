package com.epam.esm.controller.support;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.exception.InvalidControllerOutputMessage;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControllerUtils {

    private static JwtTokenProvider jwtTokenProvider;
    private static UserService service;
    private static OrderService orderService;
    private static DtoConverter<CertificateOrderDTO, CertificateOrderPOJO> orderConverter;

    @Autowired
    public ControllerUtils(JwtTokenProvider jwtTokenProvider, UserService service,
        OrderService orderService,
        DtoConverter<CertificateOrderDTO, CertificateOrderPOJO> orderConverter) {
        ControllerUtils.jwtTokenProvider = jwtTokenProvider;
        ControllerUtils.service = service;
        ControllerUtils.orderService = orderService;
        ControllerUtils.orderConverter = orderConverter;
    }

    public static void checkIsCurrentUserHaveRulesForEditThisOrder(long userId, long orderId) {
        String exceptionMessageParameter = "user id";
        String exceptionMessage = "You don't have access with action for current order";

        if (!isThisOrderBelowCurrentUser(userId, orderId)) {
            throw new ControllerException(
                new InvalidControllerOutputMessage(exceptionMessageParameter, exceptionMessage)
            );
        }
    }

    public static int getValidPaginationParam(String page, String paramName) {
        int defaultReturn = ControllerParamNames.PAGE_PARAM_NAME.equals(paramName) ?
            ControllerParamNames.DEFAULT_SIZE : ControllerParamNames.DEFAULT_PAGE;
        try {
            int paramInteger = Integer.parseInt(page);
            return paramInteger > 0 ? paramInteger : defaultReturn;
        } catch (NumberFormatException e) {
            return defaultReturn;
        }
    }

    public static boolean isThisOrderBelowCurrentUser(long userId, long orderId) {
        List<CertificateOrderDTO> orders =
            orderConverter
                .convert(orderService.findAllByOwner(userId));
        return orders.stream()
            .anyMatch(certificateOrderDTO -> certificateOrderDTO.getId() == orderId);
    }

    public static void checkUserRulesById(HttpServletRequest req, long actionUserId) {
        String exceptionMessageParameter = "user id";
        String exceptionMessage = "You don't have access with action for current user";

        String token = jwtTokenProvider.resolveToken(req);
        String username = jwtTokenProvider.getUsername(token);
        UserDTO userDTO = new UserDTO(service.findByLogin(username));

        if (userDTO.getId() != actionUserId && userDTO.getRoles().stream()
            .noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new ControllerException(
                new InvalidControllerOutputMessage(exceptionMessageParameter, exceptionMessage)
            );
        }
    }
}
