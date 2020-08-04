package com.epam.esm.controller;

import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.dto.AuthenticationRequestDto;
import com.epam.esm.dto.RegistrationUserDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private DtoConverter<RegistrationUserDTO, UserPOJO> converter;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider, UserService userService,
                                    DtoConverter<RegistrationUserDTO, UserPOJO> converter) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.converter = converter;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Map<Object, Object>> login(
        @RequestBody AuthenticationRequestDto requestDto) {
        String invalid = "Invalid username or password";
        String parameterUsername = "username";
        String parameterToken = "token";

        try {
            String username = requestDto.getLogin();
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, requestDto.getPassword())
            );
            UserDTO user = new UserDTO(userService.findByLogin(username));

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put(parameterUsername, username);
            response.put(parameterToken, token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(invalid);
        }
    }

    @PostMapping(path = "/registration")
    public ResponseEntity<EntityModel<RegistrationUserDTO>> registration(
        @Valid @RequestBody RegistrationUserDTO userDTO) {
        String invalid = "Invalid username or password";

        try {
            UserPOJO userPOJO = userService
                .create(converter.convert(userDTO));
            return new ResponseEntity<>(new RegistrationUserDTO(userPOJO).getModel(),
                HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(invalid);
        }
    }
}
