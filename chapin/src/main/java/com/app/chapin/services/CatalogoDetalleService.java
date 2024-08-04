package com.app.chapin.services;

import com.app.chapin.persistence.dtos.CatalogoDto;
import com.app.chapin.persistence.models.Catalogo;
import com.app.chapin.persistence.models.CatalogoDetalle;
import com.app.chapin.persistence.respository.CatalogoDetalleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CatalogoDetalleService {

    @Autowired
    private CatalogoDetalleRepository repository;

    public CatalogoDetalle crearCatalogo(CatalogoDto dto, Integer idCatalogo) {
        log.info("Se esta crando el catalogo {} ", dto.getNombre());
        log.info("Detalle catalogo {} ", dto.getDetalle());
        log.info("Es activo? {} ", dto.getActivo());
        CatalogoDetalle catalogoDetalle = new CatalogoDetalle();
        catalogoDetalle.setId(repository.getSecuencia());
        catalogoDetalle.setIdCatalogo(idCatalogo);
        catalogoDetalle.setNombre(dto.getNombre());
        catalogoDetalle.setDetalle(dto.getDetalle());
        catalogoDetalle.setActivo(dto.getActivo());
        catalogoDetalle.setFechaAdicion(LocalDateTime.now());
        return repository.save(catalogoDetalle);
    }

//    public CatalogoDetalle getCatalogo(Integer id, Integer idCatalogo) {
//        return repository.
//    }
//
//    public List<Catalogo> getCatalogos() {
//        return repository.findAll();
//    }
//
//    public Catalogo actualizarCatalogo(CatalogoDto dto, Integer id) {
//        Catalogo catalogo = repository.findById(id).orElseThrow();
//        catalogo.setNombre(dto.getNombre());
//        catalogo.setDetalle(dto.getDetalle());
//        catalogo.setActivo(dto.getActivo());
//        catalogo.setFechaModifico(LocalDateTime.now());
//        return repository.save(catalogo);
//    }
}
