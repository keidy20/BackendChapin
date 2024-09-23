package com.app.chapin.persistence.dtos.request;

import lombok.Data;

@Data
public class RegistroUsuarioLeccionDto {

    private String username;
    private Integer idLeccion;
    private Boolean completado;
    private String puntuacion;
}
