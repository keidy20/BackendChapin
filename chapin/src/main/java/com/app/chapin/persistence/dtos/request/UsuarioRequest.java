package com.app.chapin.persistence.dtos.request;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String nombre;
    private Integer edad;
    private String username;
    private String email;
    private String password;
}
