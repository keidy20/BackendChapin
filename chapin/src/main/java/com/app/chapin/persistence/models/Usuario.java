package com.app.chapin.persistence.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name="usuarios", schema="chapin_schema", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username","email"})}
)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "chapin_schema.secuencia_usuarios", allocationSize = 1)
    private Integer id;
    private String nombre;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    private Integer edad;
    private String password;
    private LocalDateTime fechaAdicion;
    private LocalDateTime fechaModificacion;
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
