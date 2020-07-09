package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@RequestBody UserDTO user) {
        try {
            service.create(user.dtoToPojo());
            return new ResponseEntity<>(service.findAll()
                    .stream()
                    .map(UserDTO::new)
                    .collect(Collectors.toList()), HttpStatus.CREATED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> findAll() {
        return new ResponseEntity<>(service.findAll()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
