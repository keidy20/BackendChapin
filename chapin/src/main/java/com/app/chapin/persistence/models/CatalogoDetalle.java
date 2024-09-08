package com.app.chapin.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "chapin_schema", name = "catalogo_detalle")
public class CatalogoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_catalogo_detalle")
    @SequenceGenerator(name = "seq_catalogo_detalle", sequenceName = "chapin_schema.secuencia_catalogo_detalle", allocationSize = 1)
    private Integer id;
    private Integer idCatalogo;
    private String nombre;
    private String detalle;
    private String valor;
    private Boolean activo;
    private LocalDateTime fechaAdicion;
    private LocalDateTime fechaModifico;
}
