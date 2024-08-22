package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.Ejercicios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EjercicioRepository extends JpaRepository<Ejercicios, Integer> {
}
