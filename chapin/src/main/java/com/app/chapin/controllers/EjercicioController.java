package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.EjercicioDto;
import com.app.chapin.services.EjercicioService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/audio/{id}/{forzar}")
    public ResponseEntity<?> agregarAudioLeccion (
            @PathVariable @Parameter(description = "id ejercicio") Integer id,
            @PathVariable @Parameter(description = "forzar subida") Boolean forzar
    ) {
        service.agregarAudio(id, forzar);
        return ResponseEntity.ok("Se agregaron los archivos de audio");
    }

    @GetMapping("/menu-ejercicios/{usuario}")
    public ResponseEntity<?> getMenuEjercicios(
            @PathVariable @Parameter(description = "usuario") String usuario
    ) {
        return ResponseEntity.ok(service.getMenuEjerciciosByUsuario(usuario));
    }
}
