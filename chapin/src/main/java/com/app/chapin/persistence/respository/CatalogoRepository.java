package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {

    @Query(value = "select nextval('chapin_schema.secuencia_catalogo')")
    public Integer getSecuencia();
}
