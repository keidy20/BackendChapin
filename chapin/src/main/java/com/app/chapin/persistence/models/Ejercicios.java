package com.app.chapin.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "chapin_schema", name = "ejercicios")
public class Ejercicios {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ejercicios")
    @SequenceGenerator(name = "seq_ejercicios", sequenceName = "chapin_schema.secuencia_ejercicios", allocationSize = 1)
    private Integer idEjercicio;
    private String tipoEjercicio;
    private String titulo;
    private String contenido;
    private Integer orden;
    private Integer duracionEstimada;
    private boolean activo;
    private LocalDateTime fechaAdicion;
    private LocalDateTime fechaModifico;
}
