package com.app.chapin.services;


import com.app.chapin.persistence.models.Usuarios;
import com.app.chapin.persistence.respository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository repository;

    public List<Usuarios> getAllUsuarios() {
        return repository.findAll();
    }
}
