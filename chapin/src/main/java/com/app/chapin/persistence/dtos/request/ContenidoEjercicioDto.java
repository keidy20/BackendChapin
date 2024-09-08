package com.app.chapin.persistence.dtos.request;

import lombok.Data;
@Data
public class ContenidoEjercicioDto {
    private String oracion;
    private String oracionCompleta;
    private String[] opciones;
    private String opcionCorrecta;
}
