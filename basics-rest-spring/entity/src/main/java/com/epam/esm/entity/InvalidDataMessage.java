package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Scope("prototype")
@Component
public class InvalidDataMessage {
    private String field;
    private String message;

    public InvalidDataMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public InvalidDataMessage() {
    }

    public InvalidDataMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
