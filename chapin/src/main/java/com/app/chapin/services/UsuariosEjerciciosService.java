package com.app.chapin.services;

import com.app.chapin.exceptions.NotFoundException;
import com.app.chapin.persistence.dtos.UsuarioEjercicioCompleted;
import com.app.chapin.persistence.dtos.request.RegistroUsuarioEjercicioDto;
import com.app.chapin.persistence.dtos.response.GenericResponse;
import com.app.chapin.persistence.models.*;
import com.app.chapin.persistence.respository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsuariosEjerciciosService {

    @Autowired
    private UsuariosEjerciciosRepository usuariosEjerciciosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private EjercicioRepository repository;



    public GenericResponse registrarEjercicioByUsername(RegistroUsuarioEjercicioDto dto) {

        Usuario usuario = usuariosRepository
                .findByUsername(dto.getUsername()).orElseThrow(() -> new NotFoundException("El usuario no existe"));

        Integer idUsuario = usuario.getId();

        if (!repository.existsById(dto.getIdEjercicio())) {
            throw new NotFoundException("El ejercicio no existe");
        }

        if (usuariosEjerciciosRepository.existsByUsuarioIdAndEjercicioId(idUsuario, dto.getIdEjercicio())) {
            actualizarEjercicios(idUsuario, dto.getIdEjercicio(), dto.getPuntuacion());
            return new GenericResponse(200, "Se actualizaron los registros");
        }

        UsuariosEjercicios usuariosEjercicios = new UsuariosEjercicios();
        usuariosEjercicios.setUsuarioId(idUsuario);
        usuariosEjercicios.setEjercicioId(dto.getIdEjercicio());
        usuariosEjercicios.setCompletado(dto.getCompletado());
        usuariosEjercicios.setPuntuacion(dto.getPuntuacion());

        usuariosEjerciciosRepository.save(usuariosEjercicios);

        return new GenericResponse(200, "Se completó el ejercicio");

    }

    public List<UsuarioEjercicioCompleted> getUsuarioEjerciciosCompleted(String usuario) {

        return usuariosEjerciciosRepository.getEjerciciosCompletedByUsuario(usuario);
    }

    private void actualizarEjercicios(Integer idUsuario, Integer idEjercicio, String puntuacion) {
        UsuariosEjercicios usuariosEjercicios = usuariosEjerciciosRepository.findByUsuarioIdAndEjercicioId(idUsuario, idEjercicio);
        usuariosEjercicios.setPuntuacion(puntuacion);
        usuariosEjerciciosRepository.save(usuariosEjercicios);
    }

    public long cantidadEjercicios() {
        return repository.findAll()
                .stream()
                .filter((ejercicios -> ejercicios.getTipoEjercicio().matches("CO||CP||QZ")))
                .count();
    }

    public Integer cantidadEjerciciosCompletadosByUsuario(String usuario) {
        return usuariosEjerciciosRepository.cantidadEjercicios(usuario);
    }
}
