package com.app.chapin.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "chapin_schema", name = "lecciones")
public class Lecciones {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lecciones")
    @SequenceGenerator(name = "seq_lecciones", sequenceName = "chapin_schema.secuencia_lecciones", allocationSize = 1)
    private Integer idLeccion;
    private String tipoLeccion;
    private String titulo;
    private String contenido;
    private String nivel;
    private Integer orden;
    private Integer duracionEstimada;
    private boolean activo;
    private LocalDateTime fechaAdicion;
    private LocalDateTime fechaModifico;
}
