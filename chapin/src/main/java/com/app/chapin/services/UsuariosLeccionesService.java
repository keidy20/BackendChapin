package com.app.chapin.services;

import com.app.chapin.exceptions.NotFoundException;
import com.app.chapin.persistence.dtos.request.RegistroUsuarioLeccionDto;
import com.app.chapin.persistence.dtos.response.GenericResponse;
import com.app.chapin.persistence.models.Lecciones;
import com.app.chapin.persistence.models.Usuario;
import com.app.chapin.persistence.models.UsuariosLecciones;
import com.app.chapin.persistence.respository.LeccionRepository;
import com.app.chapin.persistence.respository.UsuariosLeccionesRepository;
import com.app.chapin.persistence.respository.UsuariosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsuariosLeccionesService {

    @Autowired
    private UsuariosLeccionesRepository repository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private LeccionRepository leccionRepository;

    @Autowired
    private UsuariosEjerciciosService usuariosEjerciciosService;



    public GenericResponse registrarLeccionByUsername(RegistroUsuarioLeccionDto dto) {

        Usuario usuario = usuariosRepository
                .findByUsername(dto.getUsername()).orElseThrow(() -> new NotFoundException("El usuario no existe"));

        Integer idUsuario = usuario.getId();

        if (!leccionRepository.existsById(dto.getIdLeccion())) {
            throw new NotFoundException("La leccion no existe");
        }

        if (repository.existsByUsuarioIdAndLeccionId(idUsuario, dto.getIdLeccion())) {
            actualizarLecciones(idUsuario, dto.getIdLeccion(), dto.getPuntuacion());
            return new GenericResponse(200, "Se actualizaron los registros");
        }

        UsuariosLecciones usuarioLeccion = new UsuariosLecciones();
        usuarioLeccion.setUsuarioId(idUsuario);
        usuarioLeccion.setLeccionId(dto.getIdLeccion());
        usuarioLeccion.setCompletado(dto.getCompletado());
        usuarioLeccion.setPuntuacion(dto.getPuntuacion());
        repository.save(usuarioLeccion);

        Lecciones leccionAsociada = leccionRepository.findByQuiz(dto.getIdLeccion());
        

        if (leccionAsociada != null) {
            UsuariosLecciones usuarioLeccionAsociada = new UsuariosLecciones();
            usuarioLeccionAsociada.setUsuarioId(idUsuario);
            usuarioLeccionAsociada.setLeccionId(leccionAsociada.getIdLeccion());
            usuarioLeccionAsociada.setCompletado(dto.getCompletado());
            usuarioLeccionAsociada.setPuntuacion(null);

            repository.save(usuarioLeccionAsociada);
        }


        return new GenericResponse(200, "Se completó la leccion");

    }

    private void actualizarLecciones(Integer idUsuario, Integer idLeccion, String puntuacion) {
        UsuariosLecciones usuarioLeccion = repository.findByUsuarioIdAndLeccionId(idUsuario, idLeccion);
        usuarioLeccion.setCompletado(true);
        usuarioLeccion.setPuntuacion(puntuacion);
        repository.save(usuarioLeccion);

        Lecciones leccionAsociada = leccionRepository.findByQuiz(idLeccion);

        if (leccionAsociada != null) {
            UsuariosLecciones usuarioLeccionAsociada = repository.findByUsuarioIdAndLeccionId(idUsuario, leccionAsociada.getIdLeccion());
            if (usuarioLeccionAsociada == null) {
                usuarioLeccionAsociada = new UsuariosLecciones();
                usuarioLeccionAsociada.setUsuarioId(idUsuario);
                usuarioLeccionAsociada.setLeccionId(leccionAsociada.getIdLeccion());
                usuarioLeccionAsociada.setCompletado(true);
                usuarioLeccionAsociada.setPuntuacion(puntuacion);
                repository.save(usuarioLeccionAsociada);
            } else {
                usuarioLeccionAsociada.setCompletado(true);
                usuarioLeccionAsociada.setPuntuacion(puntuacion);
                repository.save(usuarioLeccionAsociada);
            }
        }

    }

    public List<UsuariosLecciones> getLeccionesByUsuario(String username) {
        Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("El usuario no existe"));

        List<UsuariosLecciones> usuariosLecciones = repository.findByUsuarioId(usuario.getId());

        return usuariosLecciones;

    }

    public GenericResponse leccionInicialFinalizada(String usuario) {
        usuariosRepository.findByUsername(usuario).orElseThrow(() -> new NotFoundException("Usuario no existe"));

        Integer evaluacionInicial = repository.evaluacionCompletada(usuario);

        if (evaluacionInicial > 0) {
            return new GenericResponse(200, "completada");
        }

        return new GenericResponse(200, "no completado");
    }

    public boolean leccionFinalHabilitada(String usuario) {
        usuariosRepository.findByUsername(usuario).orElseThrow(() -> new NotFoundException("Usuario no existe"));

        long cantidadLecciones = leccionRepository.findAll()
                .stream()
                .filter(lecciones -> lecciones.getTipoLeccion().matches("RL||EI||QR"))
                .count();

        Integer cantidadLeccionesCompletadas = repository.cantidadLecciones(usuario);

        long cantidadEjercicios = usuariosEjerciciosService.cantidadEjercicios();

        Integer cantidadEjerciciosCompletados = usuariosEjerciciosService.cantidadEjerciciosCompletadosByUsuario(usuario);

        if (cantidadLecciones == cantidadLeccionesCompletadas && cantidadEjercicios == cantidadEjerciciosCompletados) {
            return true;
        }

        return false;
    }
}
