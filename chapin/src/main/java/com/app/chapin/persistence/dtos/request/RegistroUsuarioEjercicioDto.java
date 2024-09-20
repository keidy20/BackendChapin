package com.app.chapin.persistence.dtos.request;

import lombok.Data;

@Data
public class RegistroUsuarioEjercicioDto {

    private String username;
    private Integer idEjercicio;
    private Boolean completado;
    private String puntuacion;
}
