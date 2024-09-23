package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.UsuariosEjercicios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosEjerciciosRepository extends JpaRepository<UsuariosEjercicios, Integer> {

    public boolean existsByUsuarioIdAndEjercicioId(Integer usuarioId, Integer ejercicioId);
}
