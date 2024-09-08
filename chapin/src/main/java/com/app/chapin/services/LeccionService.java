package com.app.chapin.services;

import com.app.chapin.persistence.dtos.request.LeccionDto;
import com.app.chapin.persistence.dtos.request.ContenidoDTO;
import com.app.chapin.persistence.models.Lecciones;
import com.app.chapin.persistence.respository.LeccionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LeccionService {

    @Autowired
    private LeccionRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Gson gson = new Gson();

    public Lecciones crearLeccion(LeccionDto dto) {
        Lecciones leccion = new Lecciones();
        leccion.setTipoLeccion(dto.getTipoLeccion());
        leccion.setTitulo(dto.getTitulo());

        String contenidoJson = gson.toJson(dto.getContenido());
        leccion.setContenido(contenidoJson);

        leccion.setNivel(dto.getNivel());
        leccion.setDuracionEstimada(dto.getDuracionEstimada());
        leccion.setActivo(dto.isActivo());
        leccion.setOrden(dto.getOrden());
        leccion.setFechaAdicion(LocalDateTime.now());
        return repository.save(leccion);
    }

    public LeccionDto getLeccionDto(Integer id) {
        Lecciones leccion = repository.findById(id).orElse(null);
        if (leccion == null) {
            return null;
        }

        return convertirLeccionAReponseDto(leccion);
    }

    public List<LeccionDto> getLeccionesDtos() {
        List<Lecciones> lecciones = repository.findAll();
        return lecciones.stream()
                .map(this::convertirLeccionAReponseDto)
                .collect(Collectors.toList());
    }

    public Lecciones actualizarLecciones(LeccionDto dto, Integer id) {
        Lecciones leccion = repository.findById(id).orElseThrow();

        leccion.setTipoLeccion(dto.getTipoLeccion());
        leccion.setTitulo(dto.getTitulo());

        try {
            String contenidoJson = objectMapper.writeValueAsString(dto.getContenido());
            leccion.setContenido(contenidoJson);
        } catch (JsonProcessingException e) {
            log.error("Error al serializar ContenidoDTO a JSON", e);
        }

        leccion.setNivel(dto.getNivel());
        leccion.setDuracionEstimada(dto.getDuracionEstimada());
        leccion.setActivo(dto.isActivo());
        leccion.setOrden(dto.getOrden());
        leccion.setFechaModifico(LocalDateTime.now());
        return repository.save(leccion);
    }

    private LeccionDto convertirLeccionAReponseDto(Lecciones leccion) {
        LeccionDto dto = new LeccionDto();
        dto.setTipoLeccion(leccion.getTipoLeccion());
        dto.setTitulo(leccion.getTitulo());
        dto.setNivel(leccion.getNivel());
        dto.setDuracionEstimada(leccion.getDuracionEstimada());
        dto.setActivo(leccion.isActivo());
        dto.setOrden(leccion.getOrden());

        // Deserializar el JSON de 'contenido' a un ContenidoDTO
        //ContenidoDTO contenidoDto = objectMapper.readValue(leccion.getContenido(), ContenidoDTO.class);
        ContenidoDTO contenidoDto = gson.fromJson(leccion.getContenido(), ContenidoDTO.class);
        dto.setContenido(contenidoDto);

        return dto;
    }

    public Lecciones getLecciones(Integer id) {
        return null;
    }
}
