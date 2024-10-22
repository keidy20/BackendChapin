package com.app.chapin.services;

import com.app.chapin.persistence.dtos.request.LoginRequest;
import com.app.chapin.persistence.dtos.response.AuthResponse;
import com.app.chapin.persistence.dtos.response.GenericResponse;
import com.app.chapin.persistence.models.Usuario;
import com.app.chapin.persistence.respository.UsuariosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuariosRepository repository;

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            log.info("Usuario request {}", request.getUsername());
            UserDetails usuario = repository.findByUsername(request.getUsername()).orElseThrow();
            String token = jwtService.getToken(usuario);
            return AuthResponse.builder()
                    .token(token)
                    .build();
        }  catch (BadCredentialsException exception) {
            log.error("Credenciales invalidas {}", exception);
            throw exception;
        } catch (AuthenticationException exception) {
            log.error("Credenciales invalidas {}", exception);
            throw exception;
        }

    }
}
