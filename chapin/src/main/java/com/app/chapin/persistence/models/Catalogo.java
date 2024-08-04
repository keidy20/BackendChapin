package com.app.chapin.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "chapin_schema", name = "catalogo")
public class Catalogo {

    @Id
    private Integer id;
    private String nombre;
    private String detalle;
    private Boolean activo;
    private LocalDateTime fechaAdicion;
    private LocalDateTime fechaModifico;
}
