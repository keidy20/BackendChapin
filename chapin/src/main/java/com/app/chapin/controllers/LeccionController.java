package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.CatalogoDto;
import com.app.chapin.persistence.dtos.request.LeccionDto;
import com.app.chapin.persistence.dtos.response.UsuarioLeccionesDto;
import com.app.chapin.persistence.models.Catalogo;
import com.app.chapin.persistence.models.Lecciones;
import com.app.chapin.services.LeccionService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public LeccionDto getLecciones(@PathVariable @Parameter(description  = "id lecciones") Integer id) {
        return service.getLecciones(id);
    }
    @GetMapping("/all")
    public List<LeccionDto> getLecciones() {
        return service.getLeccionesDtos();
    }

    @GetMapping("/all/{username}")
    public List<UsuarioLeccionesDto> getLeccionesByUsername(@PathVariable @Parameter(description  = "username") String username) {
        return service.getLeccionesByUsername(username);
    }

    @PutMapping("/{id}")
    public Lecciones actualizarLecciones(
            @PathVariable @Parameter(description = "id lecciones") Integer id,
            @RequestBody LeccionDto dto
    ) {
        return service.actualizarLecciones(dto, id);
    }

    @PostMapping("/audio/{id}/{forzar}")
    public ResponseEntity<?> agregarAudioLeccion (
            @PathVariable @Parameter(description = "id leccion") Integer id,
            @PathVariable @Parameter(description = "forzar subida") Boolean forzar
    ) {
        service.agregarAudio(id, forzar);
        return ResponseEntity.ok("Se agregaron los archivos de audio");
    }
}
