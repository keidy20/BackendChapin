package com.app.chapin.persistence.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(schema = "chapin_schema", name = "usuarios_ejercicios")
public class UsuariosEjercicios {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario_ejercicios")
    @SequenceGenerator(name = "seq_usuario_ejercicios", sequenceName = "chapin_schema.secuencia_usuario_ejercicios", allocationSize = 1)
    private Integer id;
    private Integer usuarioId;
    private Integer ejercicioId;
    private Boolean completado;
    private String puntuacion;
}
