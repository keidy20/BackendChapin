package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.CatalogoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CatalogoDetalleRepository extends JpaRepository<CatalogoDetalle, Integer> {

    @Query(value = "select nextval('chapin_schema.secuencia_catalogo_detalle')")
    public Integer getSecuencia();
}
