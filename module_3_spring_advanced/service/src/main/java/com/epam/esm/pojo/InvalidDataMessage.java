package com.epam.esm.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class InvalidDataMessage {
    private String field;
    private String message;

    public InvalidDataMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public InvalidDataMessage(String message) {
        this.message = message;
    }
}
