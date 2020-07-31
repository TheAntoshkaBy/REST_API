package com.epam.esm.exception;

import com.epam.esm.exception.entity.InvalidDataOutputMessage;
import com.epam.esm.pojo.InvalidDataMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ShopRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public List<InvalidDataMessage> ShopServiceHandler(ServiceException e) {
        return e.getMessages();
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
    public List<InvalidDataOutputMessage> ShopRepositoryHandler(RepositoryException e) {
        return e.getMessages();
    }

    @ExceptionHandler(ControllerException.class)
    @ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
    public List<InvalidControllerOutputMessage> ShopControllerHandler(ControllerException e) {
        return e.getMessages();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
