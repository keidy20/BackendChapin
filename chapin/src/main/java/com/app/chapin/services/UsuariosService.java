package com.app.chapin.services;


import com.app.chapin.exceptions.UserExistException;
import com.app.chapin.persistence.dtos.request.UsuarioRequest;
import com.app.chapin.persistence.dtos.response.AuthResponse;
import com.app.chapin.persistence.dtos.response.UsuarioResponse;
import com.app.chapin.persistence.models.Role;
import com.app.chapin.persistence.models.Usuario;
import com.app.chapin.persistence.respository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public List<Usuario> getAllUsuarios() {
        return repository.findAll();
    }

    @Transactional
    public AuthResponse crearUsuario(UsuarioRequest usuario) throws UserExistException {

        Optional<Usuario> usuarioTemp = repository.findByUsername(usuario.getUsername());
        if (usuarioTemp.isPresent()) {
            throw new UserExistException("El usuario ya esta registrado");
        }

        usuarioTemp = repository.findByEmail(usuario.getEmail());
        if (usuarioTemp.isPresent()) {
            throw new UserExistException("El email ya esta registrado");
        }

        String hashedPassword = passwordEncoder.encode(usuario.getPassword());

        Usuario usuarioBD = new Usuario();
        usuarioBD.setNombre(usuario.getNombre());
        usuarioBD.setEdad(usuario.getEdad());
        usuarioBD.setUsername(usuario.getUsername());
        usuarioBD.setEmail(usuario.getEmail());
        usuarioBD.setPassword(hashedPassword);
        usuarioBD.setFechaAdicion(LocalDateTime.now());
        usuarioBD.setRole(Role.USER);
        //return null;
        repository.save(usuarioBD);

        return AuthResponse.builder()
                .token(jwtService.getToken(usuarioBD))
                .build();
    }
}
