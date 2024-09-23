package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.UsuariosLecciones;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosLeccionesRepository extends JpaRepository<UsuariosLecciones, Integer> {

    public boolean existsByUsuarioIdAndLeccionId(Integer usuarioId, Integer leccionId);
}
