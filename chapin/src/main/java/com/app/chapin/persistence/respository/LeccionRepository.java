package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.Lecciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LeccionRepository extends JpaRepository<Lecciones, Integer> {

}
