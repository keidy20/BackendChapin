package com.app.chapin.services;

import com.app.chapin.exceptions.NotFoundException;
import com.app.chapin.persistence.dtos.request.RegistroUsuarioEjercicioDto;
import com.app.chapin.persistence.dtos.request.RegistroUsuarioLeccionDto;
import com.app.chapin.persistence.dtos.response.GenericResponse;
import com.app.chapin.persistence.models.Usuario;
import com.app.chapin.persistence.models.UsuariosEjercicios;
import com.app.chapin.persistence.models.UsuariosLecciones;
import com.app.chapin.persistence.respository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsuariosEjerciciosService {

    @Autowired
    private UsuariosEjerciciosRepository repository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;



    public GenericResponse registrarEjercicioByUsername(RegistroUsuarioEjercicioDto dto) {

        Usuario usuario = usuariosRepository
                .findByUsername(dto.getUsername()).orElseThrow(() -> new NotFoundException("El usuario no existe"));

        Integer idUsuario = usuario.getId();

        if (!ejercicioRepository.existsById(dto.getIdEjercicio())) {
            throw new NotFoundException("El ejercicio no existe");
        }

        if (repository.existsByUsuarioIdAndEjercicioId(idUsuario, dto.getIdEjercicio())) {
            throw new NotFoundException("El ejercicio con ese usuario ya fue registrado");
        }

        UsuariosEjercicios usuariosEjercicios = new UsuariosEjercicios();
        usuariosEjercicios.setUsuarioId(idUsuario);
        usuariosEjercicios.setEjercicioId(dto.getIdEjercicio());
        usuariosEjercicios.setCompletado(dto.getCompletado());
        usuariosEjercicios.setPuntuacion(dto.getPuntuacion());

        repository.save(usuariosEjercicios);

        return new GenericResponse(200, "Se completó el ejercicio");

    }
}
