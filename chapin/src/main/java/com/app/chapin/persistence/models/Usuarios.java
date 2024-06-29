package com.app.chapin.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="usuarios", schema="chapin_schema")
public class Usuarios {

    @Id
    private Integer id;
    private String nombre;
    private String email;
    private String password;
    private LocalDateTime fechaAdicion;
    private LocalDateTime fechaModificacion;
}
