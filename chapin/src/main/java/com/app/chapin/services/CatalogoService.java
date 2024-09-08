package com.app.chapin.services;

import com.app.chapin.exceptions.NotFoundException;
import com.app.chapin.persistence.dtos.request.CatalogoDetalleDto;
import com.app.chapin.persistence.dtos.request.CatalogoDto;
import com.app.chapin.persistence.models.Catalogo;
import com.app.chapin.persistence.models.CatalogoDetalle;
import com.app.chapin.persistence.respository.CatalogoDetalleRepository;
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

    @Autowired
    private CatalogoDetalleRepository detalleRepository;

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

    public CatalogoDetalle crearCatalogoDetalle(CatalogoDetalleDto dto) {
        CatalogoDetalle detalle = new CatalogoDetalle();
        detalle.setIdCatalogo(dto.getIdCatalogo());
        detalle.setNombre(dto.getNombre());
        detalle.setDetalle(dto.getDetalle());
        detalle.setValor(dto.getValor());
        detalle.setActivo(dto.isActivo());
        detalle.setFechaAdicion(LocalDateTime.now());

        return detalleRepository.save(detalle);
    }

    public CatalogoDetalle getCatalogoDetalleById(Integer idPadre, Integer id) {
        return detalleRepository.findByIdPadreAndId(idPadre, id)
                .orElseThrow(() -> new NotFoundException("El detalle del catalogo no existe"));
    }

    public List<CatalogoDetalle> getCatalogoDetalleByIdPadre(Integer idPadre) {

        return detalleRepository.findByIdCatalogo(idPadre)
                .orElseThrow(() -> new NotFoundException("El catalogo padre no existe"));
    }
}
