package com.app.chapin.persistence.dtos.request;

import lombok.Data;

@Data
public class ContenidoDTO {
    private String letra;
    private String[][] silabas;
    private String[] palabra;
    private String[] sentencia;
}
