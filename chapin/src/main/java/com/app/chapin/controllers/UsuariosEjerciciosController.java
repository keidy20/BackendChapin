package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.RegistroUsuarioEjercicioDto;
import com.app.chapin.persistence.dtos.request.RegistroUsuarioLeccionDto;
import com.app.chapin.services.UsuariosEjerciciosService;
import com.app.chapin.services.UsuariosLeccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios_ejercicios")
public class UsuariosEjerciciosController {

    @Autowired
    private UsuariosEjerciciosService service;

    @PostMapping("/registrar_ejercicio_by_username")
    public ResponseEntity<?> registrarEjercicioByUsername(@RequestBody RegistroUsuarioEjercicioDto dto) {

        try {
            return ResponseEntity.ok(service.registrarEjercicioByUsername(dto));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
