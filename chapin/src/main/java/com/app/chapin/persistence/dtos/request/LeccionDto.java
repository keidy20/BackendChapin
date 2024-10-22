package com.app.chapin.persistence.dtos.request;

import lombok.Data;

@Data
public class LeccionDto {
    private Integer id;
    private String  tipoLeccion;
    private String titulo;
    private Object contenido;
    private String nivel;
    private Integer orden;
    private Integer duracionEstimada;
    private boolean activo;
    private Integer quiz;
}
