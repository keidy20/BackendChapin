package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.dtos.UsuarioEjercicioCompleted;
import com.app.chapin.persistence.models.UsuariosEjercicios;
import com.app.chapin.persistence.models.UsuariosLecciones;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuariosEjerciciosRepository extends JpaRepository<UsuariosEjercicios, Integer> {

    public boolean existsByUsuarioIdAndEjercicioId(Integer usuarioId, Integer ejercicioId);

    @Query(value = "select id_ejercicio, tipo_ejercicio from chapin_schema.ejercicios e inner join chapin_schema.usuarios_ejercicios ue on e.id_ejercicio = ue.ejercicio_id \n" +
            "inner join chapin_schema.usuarios u on u.id = ue.usuario_id \n" +
            "where u.username = :usuario", nativeQuery = true)
    public List<UsuarioEjercicioCompleted> getEjerciciosCompletedByUsuario(@Param("usuario") String usuario);

    public UsuariosEjercicios findByUsuarioIdAndEjercicioId(Integer usuarioId, Integer ejercicioId);

    @Query(value = "select count(*) from chapin_schema.ejercicios e inner join chapin_schema.usuarios_ejercicios ue on e.id_ejercicio = ue.ejercicio_id \n" +
            "inner join chapin_schema.usuarios u on u.id = ue.usuario_id \n" +
            "where u.username = :usuario and tipo_ejercicio in ('CO', 'CP', 'QZ')", nativeQuery = true)
    public Integer cantidadEjercicios(@Param("usuario") String usuario);
}
