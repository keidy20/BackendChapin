package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.EjercicioDto;
import com.app.chapin.services.EjercicioService;
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
    public EjercicioDto crearEjercicio(@RequestBody EjercicioDto dto){
        return service.crearEjercicio(dto);
    }

    @GetMapping("/{id}")
    public EjercicioDto getEjercicio(@PathVariable @Parameter(description = "ID del ejercicio") Integer id) {
        return service.getEjercicioDto(id);
    }

    @GetMapping("/all")
    public List<EjercicioDto> getEjercicios() {
        return service.getEjercicioDtos();
    }

    @PutMapping("/{id}")
    public EjercicioDto actualizarEjercicio(
            @PathVariable @Parameter(description = "ID del ejercicio") Integer id,
            @RequestBody EjercicioDto dto
    ) {
        return service.actualizarEjercicio(dto, id);
    }
}
