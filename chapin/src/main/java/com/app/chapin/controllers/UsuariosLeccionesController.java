package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.RegistroUsuarioLeccionDto;
import com.app.chapin.services.UsuariosLeccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
