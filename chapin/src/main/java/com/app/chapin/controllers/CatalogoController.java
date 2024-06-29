package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.CatalogoDto;
import com.app.chapin.persistence.models.Catalogo;
import com.app.chapin.services.CatalogoService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService service;

    @GetMapping("/{id}")
    public Catalogo getCatalogo(@PathVariable @Parameter(description  = "id catalogo") Integer id) {
        return service.getCatalogo(id);
    }

    @GetMapping("/all")
    public List<Catalogo> getCatalogos() {
        return service.getCatalogos();
    }

    @PostMapping
    public Catalogo crearCatalogo(CatalogoDto dto) {
        return service.crearCatalogo(dto);
    }

    @PutMapping("/{id}")
    public Catalogo actualizarCatalogo(
            @PathVariable @Parameter(description = "id catalogo") Integer id,
            @RequestBody CatalogoDto dto
    ) {
        return service.actualizarCatalogo(dto, id);
    }

}
