package com.app.chapin.persistence.dtos.response;

import lombok.Data;

@Data
public class MenuEjercicioDto {
    private String nombre;
    private String titulo;
    private String route;
    private boolean completado;
}
