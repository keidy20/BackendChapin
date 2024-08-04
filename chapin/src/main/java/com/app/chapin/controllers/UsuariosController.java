package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.UsuarioRequest;
import com.app.chapin.persistence.models.Usuario;
import com.app.chapin.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService service;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return service.getAllUsuarios();
    }

}
