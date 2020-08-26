package com.epam.esm.controller.support;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ControllerBadRequestException;
import com.epam.esm.exception.InvalidControllerOutputMessage;
import com.epam.esm.pojo.CertificateOrderPOJO;
import com.epam.esm.controller.security.jwt.JwtTokenProvider;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.GThreadHelper;

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
        String exceptionMessage = "You don't have access with action for current order";
        String exceptionBadId = "Id must be more than 0";

        if(userId <= 0 || orderId <= 0){
            throw new ControllerBadRequestException(
                new InvalidControllerOutputMessage(exceptionBadId)
            );
        }

        if (!isThisOrderBelowCurrentUser(userId, orderId)) {
            throw new ControllerBadRequestException(
                new InvalidControllerOutputMessage(exceptionMessage)
            );
        }
    }

    public static int getValidPaginationParam(String param, String paramName) {
        String exceptionMessageMoreThanNull = paramName + " must be more than null!";
        String exceptionMessage = paramName + " must correct number!";

        int defaultReturn = ControllerParamNames.PAGE_PARAM_NAME.equals(paramName) ?
            ControllerParamNames.DEFAULT_PAGE : ControllerParamNames.DEFAULT_SIZE;
        if(param == null){
            return defaultReturn;
        }
        try {
            int paramInteger = Integer.parseInt(param);
            if(paramInteger > 0){
                return paramInteger;
            }else {
                throw new ControllerBadRequestException(
                    new InvalidControllerOutputMessage(exceptionMessageMoreThanNull)
                );
            }
        } catch (NumberFormatException e) {
            throw new ControllerBadRequestException(
                new InvalidControllerOutputMessage(exceptionMessage)
            );
        }
    }

    public static boolean isThisOrderBelowCurrentUser(long userId, long orderId) {
        List<CertificateOrderDTO> orders =
            orderConverter.convert(orderService.findAllByOwner(userId));

        return orders.stream()
            .anyMatch(certificateOrderDTO -> certificateOrderDTO.getId() == orderId);
    }

    public static void checkUserRulesById(HttpServletRequest req, long actionUserId) {
        String exceptionMessage = "You don't have access with action for current user";
        String exceptionBadId = "Id must be more than 0";

        String token = jwtTokenProvider.resolveToken(req);
        String username = jwtTokenProvider.getUsername(token);
        UserDTO userDTO = new UserDTO(service.findByLogin(username));
        if(actionUserId <= 0){
            throw new ControllerBadRequestException(
                new InvalidControllerOutputMessage(exceptionBadId)
            );
        }

        if (userDTO.getId() != actionUserId && userDTO.getRoles().stream()
            .noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new ControllerBadRequestException(
                new InvalidControllerOutputMessage(exceptionMessage)
            );
        }
    }

    public static void checkIsPageCorrect(int page){
        String exceptionMessage = "Page Count must be more than 0!";
        if(page <= 0){
            throw new ControllerBadRequestException(
                new InvalidControllerOutputMessage(exceptionMessage)
            );
        }
    }

    public static void checkIsSizeCorrect(int size){
        String exceptionMessage = "Size Count must be more than 0!";
        if(size <= 0){
            throw new ControllerBadRequestException(
                new InvalidControllerOutputMessage(exceptionMessage)
            );
        }
    }
}
