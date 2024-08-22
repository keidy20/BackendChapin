package com.app.chapin.services;
import com.app.chapin.persistence.dtos.request.EjercicioDto;
import com.app.chapin.persistence.models.Ejercicios;
import com.app.chapin.persistence.respository.EjercicioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service

public class EjercicioService {

    @Autowired
    private EjercicioRepository repository;

    public Ejercicios crearEjercicio(EjercicioDto dto) {
        Ejercicios ejercicio = new Ejercicios();
        ejercicio.setTipoEjercicio(dto.getTipoEjercicio());
        ejercicio.setTitulo(dto.getTitulo());
        ejercicio.setContenido(dto.getContenido());
        ejercicio.setDuracionEstimada(dto.getDuracionEstimada());
        ejercicio.setActivo(dto.isActivo());
        ejercicio.setOrden(dto.getOrden());
        ejercicio.setFechaAdicion(LocalDateTime.now());
        return repository.save(ejercicio);
    }
    public Ejercicios getEjercicios(Integer id) {
        System.out.println("Obteniendo ejercicios");
        return repository.findById(id).orElse(null);
    }

    public List<Ejercicios> getEjercicios() {
        return repository.findAll();
    }

    public Ejercicios actualizarEjercicios(EjercicioDto dto, Integer id) {
        log.info("actualizando ejercicios");
        Ejercicios ejercicio = repository.findById(id).orElseThrow();
        ejercicio.setTipoEjercicio(dto.getTipoEjercicio());
        ejercicio.setTitulo(dto.getTitulo());
        ejercicio.setContenido(dto.getContenido());
        ejercicio.setDuracionEstimada(dto.getDuracionEstimada());
        ejercicio.setActivo(dto.isActivo());
        ejercicio.setOrden(dto.getOrden());
        ejercicio.setFechaAdicion(LocalDateTime.now());
        return repository.save(ejercicio);
    }
}
