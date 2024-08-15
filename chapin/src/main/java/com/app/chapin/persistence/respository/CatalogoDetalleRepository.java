package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.CatalogoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CatalogoDetalleRepository extends JpaRepository<CatalogoDetalle, Integer> {

    @Query(value = "select nextval('chapin_schema.secuencia_catalogo_detalle')")
    public Integer getSecuencia();

    @Query(value = "select * from chapin_schema.catalogo_detalle where id_catalogo = :idCatalogo and id = :id", nativeQuery = true)
    public Optional<CatalogoDetalle> findByIdPadreAndId(@Param("idCatalogo") Integer idCatalogo, @Param("id") Integer id);

    public Optional<List<CatalogoDetalle>> findByIdCatalogo(Integer idPadre);
}
