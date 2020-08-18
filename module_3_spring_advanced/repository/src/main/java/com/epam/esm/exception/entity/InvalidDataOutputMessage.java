package com.epam.esm.exception.entity;

import lombok.Data;

@Data
public class InvalidDataOutputMessage {

    private String field;
    private String message;

    public InvalidDataOutputMessage(String field, String message) {
        this.message = message;
        this.field = field;
    }
}
