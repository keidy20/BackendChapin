package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.RegistroUsuarioLeccionDto;
import com.app.chapin.services.UsuariosLeccionesService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios_lecciones")
public class UsuariosLeccionesController {

    @Autowired
    private UsuariosLeccionesService service;

    @PostMapping("/registrar_leccion_by_username")
    public ResponseEntity<?> registrarLeccionByUsername(@RequestBody RegistroUsuarioLeccionDto dto) {

        try {
            return ResponseEntity.ok(service.registrarLeccionByUsername(dto));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/leccion-inicial-finalizada/{usuario}")
    public ResponseEntity<?> leccionInicialFinalizada(@PathVariable @Parameter(description = "usuario") String usuario) {
        try {
            return ResponseEntity.ok(service.leccionInicialFinalizada(usuario));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("leccion-final-habilitada/{usuario}")
    public ResponseEntity<?> leccionFinalHabilitada(@PathVariable @Parameter(description = "usuario") String usuario) {
        try {
            return ResponseEntity.ok(service.leccionFinalHabilitada(usuario));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
