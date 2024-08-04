package com.app.chapin.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity
@Table(name="password_reset_token", schema="chapin_schema", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"token"})}
)
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_password")
    @SequenceGenerator(name = "seq_password", sequenceName = "chapin_schema.secuencia_password_reset", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    private String token;
    private Date expiracion;

    @ManyToOne
    private Usuario usuario;
}
