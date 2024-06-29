package com.app.chapin.controllers;

import com.app.chapin.persistence.models.Usuarios;
import com.app.chapin.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService service;

    @GetMapping
    public List<Usuarios> getAllUsuarios() {
        return service.getAllUsuarios();
    }
}
