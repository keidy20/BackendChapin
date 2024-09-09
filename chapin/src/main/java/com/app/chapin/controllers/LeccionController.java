package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.CatalogoDto;
import com.app.chapin.persistence.dtos.request.LeccionDto;
import com.app.chapin.persistence.models.Catalogo;
import com.app.chapin.persistence.models.Lecciones;
import com.app.chapin.services.LeccionService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecciones")
public class LeccionController {

    @Autowired
    private LeccionService service;

    @PostMapping
    public Lecciones crearLeccion(@RequestBody LeccionDto dto){
        return service.crearLeccion(dto);
    }

    @GetMapping("/{id}")
    public Lecciones getLecciones(@PathVariable @Parameter(description  = "id lecciones") Integer id) {
        return service.getLecciones(id);
    }
    @GetMapping("/all")
    public List<LeccionDto> getLecciones() {
        return service.getLeccionesDtos();
    }

    @PutMapping("/{id}")
    public Lecciones actualizarLecciones(
            @PathVariable @Parameter(description = "id lecciones") Integer id,
            @RequestBody LeccionDto dto
    ) {
        return service.actualizarLecciones(dto, id);
    }
}
