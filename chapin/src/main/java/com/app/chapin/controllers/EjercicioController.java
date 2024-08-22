package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.EjercicioDto;
import com.app.chapin.persistence.dtos.request.LeccionDto;
import com.app.chapin.persistence.models.Ejercicios;
import com.app.chapin.persistence.models.Lecciones;
import com.app.chapin.services.EjercicioService;
import com.app.chapin.services.LeccionService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ejercicios")

public class EjercicioController {
    @Autowired
    private EjercicioService service;

    @PostMapping
    public Ejercicios crearEjercicio(@RequestBody EjercicioDto dto){
        return service.crearEjercicio(dto);
    }
    @GetMapping("/{id}")
    public Ejercicios getEjercicios(@PathVariable @Parameter(description  = "id ejercicios") Integer id) {
        return service.getEjercicios(id);
    }
    @GetMapping("/all")
    public List<Ejercicios> getEjercicios() {
        return service.getEjercicios();
    }

    @PutMapping("/{id}")
    public Ejercicios actualizarLecciones(
            @PathVariable @Parameter(description = "id ejercicios") Integer id,
            @RequestBody EjercicioDto dto
    ) {
        return service.actualizarEjercicios(dto, id);
    }
}
