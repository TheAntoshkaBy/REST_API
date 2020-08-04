package com.epam.esm.controller.support.impl;

import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.pojo.UserPOJO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements DtoConverter<UserDTO, UserPOJO> {

    @Override
    public List<UserDTO> convert(List<UserPOJO> users) {
        return users
            .stream()
            .map(UserDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    public UserPOJO convert(UserDTO user) {
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
}
