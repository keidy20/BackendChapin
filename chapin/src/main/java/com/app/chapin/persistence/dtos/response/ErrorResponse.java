package com.app.chapin.persistence.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private String message;
}
