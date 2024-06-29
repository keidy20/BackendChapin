package com.app.chapin.services;

import com.app.chapin.persistence.dtos.CatalogoDto;
import com.app.chapin.persistence.models.Catalogo;
import com.app.chapin.persistence.respository.CatalogoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CatalogoService {

    @Autowired
    private CatalogoRepository repository;

    public Catalogo crearCatalogo(CatalogoDto dto) {
        log.info("Creacion catalogo padre");
        log.info("Nombre del catalogo {}", dto.getNombre());
         Catalogo catalogo = new Catalogo();
         catalogo.setId(repository.getSecuencia());
         catalogo.setNombre(dto.getNombre());
         catalogo.setDetalle(dto.getDetalle());
         catalogo.setActivo(dto.getActivo());
         catalogo.setFechaAdicion(LocalDateTime.now());
         return repository.save(catalogo);
    }

    public Catalogo getCatalogo(Integer id) {
        System.out.println("Obteniendo catalogo");
        return repository.findById(id).orElse(null);
    }

    public List<Catalogo> getCatalogos() {
        return repository.findAll();
    }

    public Catalogo actualizarCatalogo(CatalogoDto dto, Integer id) {
        log.info("actualizando catalogo");
        log.info("Id catalogo {}", id);
        Catalogo catalogo = repository.findById(id).orElseThrow();
        catalogo.setNombre(dto.getNombre());
        catalogo.setDetalle(dto.getDetalle());
        catalogo.setActivo(dto.getActivo());
        catalogo.setFechaModifico(LocalDateTime.now());
        return repository.save(catalogo);
    }
}
