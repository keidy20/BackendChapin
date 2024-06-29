package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {
}
