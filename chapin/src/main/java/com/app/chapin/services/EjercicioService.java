package com.app.chapin.services;

import com.app.chapin.exceptions.NotFoundException;
import com.app.chapin.persistence.dtos.AudioDto;
import com.app.chapin.persistence.dtos.ByteArrayMultipartFile;
import com.app.chapin.persistence.dtos.request.EjercicioDto;
import com.app.chapin.persistence.dtos.request.ContenidoEjercicioDto;
import com.app.chapin.persistence.models.Ejercicios;
import com.app.chapin.persistence.respository.EjercicioRepository;
import com.app.chapin.utils.Constantes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EjercicioService {

    @Autowired
    private EjercicioRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gson = new Gson();

    @Autowired
    private TextToSpeechService textToSpeechService;

    @Autowired
    private StorageService storageService;

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
        dto.setId(ejercicio.getIdEjercicio());
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

    public void agregarAudio(Integer id, Boolean forzar) {
        
        Ejercicios ejercicio = repository.findById(id).orElseThrow(() -> new NotFoundException("No se encontro el ejercicio"));

        subirAudioEjercicio(ejercicio, forzar);
    }

    private void subirAudioEjercicio(Ejercicios ejercicio, Boolean forzar) {

        JsonElement jsonElement = JsonParser.parseString(ejercicio.getContenido());

        String path = "ejercicios/" + ejercicio.getTipoEjercicio()+ "_" + ejercicio.getIdEjercicio() + "/audios/";

        List<AudioDto> audios = new ArrayList<>();

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("audios") && !forzar) {
                log.info("Ya existen audios cargados en lecciones");
                return;
            }
            JsonArray lecciones = jsonObject.getAsJsonArray("Ejercicios");

            lecciones.forEach((element) -> {
                JsonObject object = element.getAsJsonObject();
                String id = object.get("id").getAsString();
                String leccionAudio = object.get("audio").getAsString();
                String fileName = "EJERCICIO_".concat(id);
                log.info("Id: {}, ejercicioAudio: {}", id, leccionAudio);
                byte[] audioBytes = textToSpeechService.sintetizarAudio(leccionAudio);

                MultipartFile multipartFile = new ByteArrayMultipartFile(audioBytes,fileName, Constantes.CONTENT_TYPE_AUDIO);
                String url = storageService.upload(multipartFile, path.concat(fileName));
                audios.add(new AudioDto(id, url));
            });

            log.info("Audios lecciones {}", audios);
            Type listType = new TypeToken<List<AudioDto>>() {}.getType();
            JsonElement audiosElement = gson.toJsonTree(audios, listType);
            jsonObject.add("audios", audiosElement);

            ejercicio.setContenido(gson.toJson(jsonObject));
            repository.save(ejercicio);
        }
    }
}
