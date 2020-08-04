package com.epam.esm.controller.support.impl;

import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.dto.RegistrationUserDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.pojo.UserPOJO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationConverter implements DtoConverter<RegistrationUserDTO, UserPOJO> {

    @Override
    public List<RegistrationUserDTO> convert(List<UserPOJO> users) {
        return  users
                    .stream()
                    .map(RegistrationUserDTO::new)
                    .collect(Collectors.toList());
    }

    @Override
    public UserPOJO convert(RegistrationUserDTO user) {
        return new UserPOJO(
            user.getName(),
            user.getSurname(),
            user.getLogin(),
            user.getPassword(),
            user.getRoles(),
            user.getEmail()
        );
    }
}
