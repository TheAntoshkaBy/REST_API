package com.epam.esm.controller.support;

import com.epam.esm.dto.CertificateOrderDTO;
import com.epam.esm.dto.RegistrationUserDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.exception.InvalidControllerOutputMessage;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSupporter {

    public static final String USER_ID = "User id must be null";
    public static final String ERROR_NAME = "Name must be between 2 and 70 characters!";
    public static final String ERROR_NAME_NOT_NULL = "Name must be not null!";
    public static final String ERROR_SURNAME = "Surname must be between 3 and 170 characters";
    public static final String ERROR_SURNAME_NOT_NULL = "Surname must be not null";
    public static final String ERROR_LOGIN = "Login must be between 5 and 30 characters";
    public static final String ERROR_LOGIN_NOT_NULL = "Login must be not null";
    public static final String ERROR_PASSWORD = "Password must be between 4 and 30 characters";
    public static final String ERROR_PASSWORD_NOT_NULL = "Password must be not null";
    public static final String ERROR_EMAIL = "Email should be valid";
    public static final String ERROR_EMAIL_NOT_NULL = "Email must be not null";
    public static final String ERROR_ROLES = "Roles must be null";
    private static JwtTokenProvider jwtTokenProvider;
    private static UserService service;
    private static OrderService orderService;

    @Autowired
    public UserSupporter(JwtTokenProvider jwtTokenProvider, UserService service,
        OrderService orderService) {
        UserSupporter.jwtTokenProvider = jwtTokenProvider;
        UserSupporter.service = service;
        UserSupporter.orderService = orderService;
    }

    public static List<UserDTO> userPojoListToUserDtoList(List<UserPOJO> users) {
        return users
            .stream()
            .map(UserDTO::new)
            .collect(Collectors.toList());
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

    public static void checkIsCurrentUserHaveRulesForEditThisOrder(long userId, long orderId) {
        String exceptionMessageParameter = "user id";
        String exceptionMessage = "You don't have access with action for current order";

        if (!isThisOrderBelowCurrentUser(userId, orderId)) {
            throw new ControllerException(
                new InvalidControllerOutputMessage(exceptionMessageParameter, exceptionMessage)
            );
        }
    }

    public static boolean isThisOrderBelowCurrentUser(long userId, long orderId) {
        List<CertificateOrderDTO> orders =
            OrderSupporter
                .orderPojoListToOrderDtoList(orderService.findAllByOwner(userId));
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
