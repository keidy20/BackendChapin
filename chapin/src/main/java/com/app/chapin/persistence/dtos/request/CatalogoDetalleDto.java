package com.app.chapin.persistence.dtos.request;

import lombok.Data;

@Data
public class CatalogoDetalleDto {

    private Integer idCatalogo;
    private String nombre;
    private String detalle;
    private boolean activo;
}
