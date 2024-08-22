package com.app.chapin.services;

import com.app.chapin.persistence.dtos.request.CatalogoDto;
import com.app.chapin.persistence.dtos.request.LeccionDto;
import com.app.chapin.persistence.models.Catalogo;
import com.app.chapin.persistence.models.Lecciones;
import com.app.chapin.persistence.respository.LeccionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service

public class LeccionService {

    @Autowired
    private LeccionRepository repository;

    public Lecciones crearLeccion(LeccionDto dto) {
        Lecciones leccion = new Lecciones();
        leccion.setTipoLeccion(dto.getTipoLeccion());
        leccion.setTitulo(dto.getTitulo());
        leccion.setContenido(dto.getContenido());
        leccion.setNivel(dto.getNivel());
        leccion.setDuracionEstimada(dto.getDuracionEstimada());
        leccion.setActivo(dto.isActivo());
        leccion.setOrden(dto.getOrden());
        leccion.setFechaAdicion(LocalDateTime.now());
        return repository.save(leccion);
    }
    public Lecciones getLecciones(Integer id) {
        System.out.println("Obteniendo lecciones");
        return repository.findById(id).orElse(null);
    }

    public List<Lecciones> getLecciones() {
        return repository.findAll();
    }

    public Lecciones actualizarLecciones(LeccionDto dto, Integer id) {
        log.info("actualizando lecciones");
        log.info("Id catalogo {}", id);
        Lecciones leccion = repository.findById(id).orElseThrow();
        leccion.setTipoLeccion(dto.getTipoLeccion());
        leccion.setTitulo(dto.getTitulo());
        leccion.setContenido(dto.getContenido());
        leccion.setNivel(dto.getNivel());
        leccion.setDuracionEstimada(dto.getDuracionEstimada());
        leccion.setActivo(dto.isActivo());
        leccion.setOrden(dto.getOrden());
        leccion.setFechaAdicion(LocalDateTime.now());
        return repository.save(leccion);
    }
}
