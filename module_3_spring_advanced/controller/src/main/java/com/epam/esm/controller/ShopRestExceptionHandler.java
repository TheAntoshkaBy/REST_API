package com.epam.esm.controller;

import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.entity.InvalidDataOutputMessage;
import com.epam.esm.pojo.InvalidDataMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class ShopRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public List<InvalidDataMessage> ShopServiceHandler(ServiceException e){
            return e.getMessages();
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
    public List<InvalidDataOutputMessage> ShopRepositoryHandler(RepositoryException e){
        return e.getMessages();
    }

}
