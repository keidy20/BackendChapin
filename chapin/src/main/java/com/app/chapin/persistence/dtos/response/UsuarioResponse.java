package com.app.chapin.persistence.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponse {
    private Integer id;
    private String nombre;
    private Integer edad;
}
