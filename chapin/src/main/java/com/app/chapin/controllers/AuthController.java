package com.app.chapin.controllers;

import com.app.chapin.exceptions.UserExistException;
import com.app.chapin.persistence.dtos.request.LoginRequest;
import com.app.chapin.persistence.dtos.request.UsuarioRequest;
import com.app.chapin.persistence.dtos.response.GenericResponse;
import com.app.chapin.services.AuthService;
import com.app.chapin.services.UsuariosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private UsuariosService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("Entro bien");
            return ResponseEntity.ok().body(service.login(loginRequest));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse(500, "Credenciales invalidas"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse(500, "Credenciales invalidas"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping("/crear_usuario")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        try {
            return ResponseEntity.ok().body(usuarioService.crearUsuario(usuarioRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
