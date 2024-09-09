package com.app.chapin.services;

import com.app.chapin.persistence.dtos.request.EjercicioDto;
import com.app.chapin.persistence.dtos.request.ContenidoEjercicioDto;
import com.app.chapin.persistence.models.Ejercicios;
import com.app.chapin.persistence.respository.EjercicioRepository;
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
public class EjercicioService {

    @Autowired
    private EjercicioRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gson = new Gson();

    public EjercicioDto crearEjercicio(EjercicioDto dto) {
        Ejercicios ejercicio = new Ejercicios();
        ejercicio.setTipoEjercicio(dto.getTipoEjercicio());
        ejercicio.setTitulo(dto.getTitulo());

        String contenidoJson = gson.toJson(dto.getContenido());
        ejercicio.setContenido(contenidoJson);

        ejercicio.setDuracionEstimada(dto.getDuracionEstimada());
        ejercicio.setActivo(dto.isActivo());
        ejercicio.setOrden(dto.getOrden());
        ejercicio.setFechaAdicion(LocalDateTime.now());
        repository.save(ejercicio);

        return convertirEjercicioAResponseDto(ejercicio);
    }

    public EjercicioDto getEjercicioDto(Integer id) {
        Ejercicios ejercicio = repository.findById(id).orElse(null);
        if (ejercicio == null) {
            return null;
        }
        return convertirEjercicioAResponseDto(ejercicio);
    }

    public List<EjercicioDto> getEjercicioDtos() {
        List<Ejercicios> ejercicios = repository.findAll();
        return ejercicios.stream()
                .map(this::convertirEjercicioAResponseDto)
                .collect(Collectors.toList());
    }

    public EjercicioDto actualizarEjercicio(EjercicioDto dto, Integer id) {
        Ejercicios ejercicio = repository.findById(id).orElseThrow();

        ejercicio.setTipoEjercicio(dto.getTipoEjercicio());
        ejercicio.setTitulo(dto.getTitulo());

        try {
            String contenidoJson = objectMapper.writeValueAsString(dto.getContenido());
            ejercicio.setContenido(contenidoJson);
        } catch (JsonProcessingException e) {
            log.error("Error al serializar ContenidoEjercicioDto a JSON", e);
        }

        ejercicio.setDuracionEstimada(dto.getDuracionEstimada());
        ejercicio.setActivo(dto.isActivo());
        ejercicio.setOrden(dto.getOrden());
        ejercicio.setFechaModifico(LocalDateTime.now());
        repository.save(ejercicio);

        return convertirEjercicioAResponseDto(ejercicio);
    }

    private EjercicioDto convertirEjercicioAResponseDto(Ejercicios ejercicio) {
        EjercicioDto dto = new EjercicioDto();
        dto.setTipoEjercicio(ejercicio.getTipoEjercicio());
        dto.setTitulo(ejercicio.getTitulo());
        dto.setDuracionEstimada(ejercicio.getDuracionEstimada());
        dto.setActivo(ejercicio.isActivo());
        dto.setOrden(ejercicio.getOrden());

        // Deserializar el JSON de 'contenido' a un ContenidoEjercicioDto
        Object contenidoDto = gson.fromJson(ejercicio.getContenido(), Object.class);
        dto.setContenido(contenidoDto);

        return dto;
    }

    public Ejercicios getEjercicios(Integer id) {
        return repository.findById(id).orElse(null);
    }
}
