package com.app.chapin.services;

import com.app.chapin.exceptions.NotFoundException;
import com.app.chapin.persistence.dtos.request.RegistroUsuarioLeccionDto;
import com.app.chapin.persistence.dtos.response.GenericResponse;
import com.app.chapin.persistence.models.Usuario;
import com.app.chapin.persistence.models.UsuariosLecciones;
import com.app.chapin.persistence.respository.LeccionRepository;
import com.app.chapin.persistence.respository.UsuariosLeccionesRepository;
import com.app.chapin.persistence.respository.UsuariosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsuariosLeccionesService {

    @Autowired
    private UsuariosLeccionesRepository repository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private LeccionRepository leccionRepository;



    public GenericResponse registrarLeccionByUsername(RegistroUsuarioLeccionDto dto) {

        Usuario usuario = usuariosRepository
                .findByUsername(dto.getUsername()).orElseThrow(() -> new NotFoundException("El usuario no existe"));

        Integer idUsuario = usuario.getId();

        if (!leccionRepository.existsById(dto.getIdLeccion())) {
            throw new NotFoundException("La leccion no existe");
        }

        if (repository.existsByUsuarioIdAndLeccionId(idUsuario, dto.getIdLeccion())) {
            throw new NotFoundException("La leccion con ese usuario ya fue registrado");
        }

        UsuariosLecciones usuarioLeccion = new UsuariosLecciones();
        usuarioLeccion.setUsuarioId(idUsuario);
        usuarioLeccion.setLeccionId(dto.getIdLeccion());
        usuarioLeccion.setCompletado(dto.getCompletado());
        usuarioLeccion.setPuntuacion(dto.getPuntuacion());

        repository.save(usuarioLeccion);

        return new GenericResponse(200, "Se completó la leccion");

    }
}
