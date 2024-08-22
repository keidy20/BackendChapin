package com.app.chapin.persistence.dtos.request;

import lombok.Data;

@Data
public class EjercicioDto {
    private String  tipoEjercicio;
    private String titulo;
    private String contenido;
    private Integer orden;
    private Integer duracionEstimada;
    private boolean activo;
}
