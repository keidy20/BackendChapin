package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.UsuariosLecciones;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuariosLeccionesRepository extends JpaRepository<UsuariosLecciones, Integer> {

    public boolean existsByUsuarioIdAndLeccionId(Integer usuarioId, Integer leccionId);

    public UsuariosLecciones findByUsuarioIdAndLeccionId(Integer usuarioId, Integer leccionId);

    public List<UsuariosLecciones> findByUsuarioId(Integer usuarioId);

    @Query(value = "select count(*) from chapin_schema.lecciones l inner join chapin_schema.usuarios_lecciones ul on l.id_leccion = ul.leccion_id \n" +
            "inner join chapin_schema.usuarios u on u.id = ul.usuario_id \n" +
            "where u.username = :usuario and tipo_leccion = 'EI'", nativeQuery = true)
    public Integer evaluacionCompletada(@Parameter String usuario);

    @Query(value = "select count(*) from chapin_schema.lecciones l inner join chapin_schema.usuarios_lecciones ul on l.id_leccion = ul.leccion_id \n" +
            "inner join chapin_schema.usuarios u on u.id = ul.usuario_id \n" +
            "where u.username = :usuario and tipo_leccion in ('RL','EI','QR')", nativeQuery = true)
    public Integer cantidadLecciones(@Param("usuario") String usuario);
}
