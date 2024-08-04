package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {

    @Autowired
    public Optional<Usuario> findByEmail(String email);

    @Autowired
    public Optional<Usuario> findByUsername(String username);
}
