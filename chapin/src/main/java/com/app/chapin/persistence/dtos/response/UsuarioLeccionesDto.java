package com.app.chapin.persistence.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioLeccionesDto {
    private Integer id;
    private String titulo;
    private String tipoLeccion;
    private Boolean completado;
}
