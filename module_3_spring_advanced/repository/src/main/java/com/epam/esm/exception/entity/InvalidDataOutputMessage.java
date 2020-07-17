package com.epam.esm.exception.entity;

import lombok.Data;

@Data
public class InvalidDataOutputMessage {
    private String entity;
    private String message;

    public InvalidDataOutputMessage(String entity, String message) {
        this.message = message;
        this.entity = entity;
    }
}
