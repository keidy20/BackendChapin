package com.app.chapin.services;

import com.app.chapin.exceptions.NotFoundException;
import com.app.chapin.persistence.dtos.AudioDto;
import com.app.chapin.persistence.dtos.ByteArrayMultipartFile;
import com.app.chapin.persistence.dtos.request.LeccionDto;
import com.app.chapin.persistence.dtos.response.UsuarioLeccionesDto;
import com.app.chapin.persistence.models.Lecciones;
import com.app.chapin.persistence.models.UsuariosLecciones;
import com.app.chapin.persistence.respository.LeccionRepository;
import com.app.chapin.utils.Constantes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.*;
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
public class LeccionService {

    @Autowired
    private LeccionRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Gson gson = new Gson();

    @Autowired
    private TextToSpeechService textToSpeechService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private UsuariosLeccionesService usuariosLeccionesService;

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
        dto.setId(leccion.getIdLeccion());
        dto.setTipoLeccion(leccion.getTipoLeccion());
        dto.setTitulo(leccion.getTitulo());
        dto.setNivel(leccion.getNivel());
        dto.setDuracionEstimada(leccion.getDuracionEstimada());
        dto.setActivo(leccion.isActivo());
        dto.setOrden(leccion.getOrden());
        dto.setQuiz(leccion.getQuiz());

        // Deserializar el JSON de 'contenido' a un ContenidoDTO
        //ContenidoDTO contenidoDto = objectMapper.readValue(leccion.getContenido(), ContenidoDTO.class);
        Object contenidoDto = gson.fromJson(leccion.getContenido(), Object.class);
        dto.setContenido(contenidoDto);

        return dto;
    }

    public LeccionDto getLecciones(Integer id) {
        return repository.findById(id)
                .stream()
                .map(this::convertirLeccionAReponseDto)
                .collect(Collectors.toList()).get(0);
    }

    public void agregarAudio(Integer id, Boolean forzar) {
        Lecciones leccion = repository.findById(id).orElseThrow(() -> new NotFoundException("No existe la leccion"));

        if (leccion.getTipoLeccion().matches(
                Constantes.LECCION_QUIZ + "||" + Constantes.LECCION_QUIZ_BASICA + "||" + Constantes.LECCION_QUIZ_INTERMEDIO + "||" + Constantes.LECCION_QUIZ_AVANZADA
            )) {
            subirAudioQuiz(leccion, forzar);
        }

        if (leccion.getTipoLeccion().equals(Constantes.RECONOCER_LETRA)) {
            subirAudioReconocerLetra(leccion, forzar);
        }

        if (leccion.getTipoLeccion().matches(
                Constantes.LECTURA_BASICA + "||" + Constantes.LECTURA_INTERMEDIA + "||" + Constantes.LECTURA_AVANZADA
        )) {
            subirAudioLecturas(leccion, forzar);
        }

    }

    private void subirAudioReconocerLetra(Lecciones leccion, Boolean forzar) {
        JsonElement jsonElement = JsonParser.parseString(leccion.getContenido());

        String path = "lecciones/" + leccion.getTipoLeccion() + "_" + leccion.getIdLeccion() + "/audios/";

        List<AudioDto> audios = new ArrayList<>();

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("audios") && !forzar) {
                log.info("Ya existen audios cargados en lecciones");
                return;
            }
            JsonArray lecciones = jsonObject.getAsJsonArray("lecciones");

            lecciones.forEach((element) -> {
                JsonObject object = element.getAsJsonObject();
                String id = object.get("id").getAsString();
                String leccionAudio = object.get("audio").getAsString();
                String fileName = "LECCION_".concat(id);
                log.info("Id: {}, leccionAudio: {}", id, leccionAudio);
                byte[] audioBytes = textToSpeechService.sintetizarAudio(leccionAudio);

                MultipartFile multipartFile = new ByteArrayMultipartFile(audioBytes,fileName, Constantes.CONTENT_TYPE_AUDIO);
                String url = storageService.upload(multipartFile, path.concat(fileName));
                audios.add(new AudioDto(id, url));
            });

            log.info("Audios lecciones {}", audios);
            Type listType = new TypeToken<List<AudioDto>>() {}.getType();
            JsonElement audiosElement = gson.toJsonTree(audios, listType);
            jsonObject.add("audios", audiosElement);

            leccion.setContenido(gson.toJson(jsonObject));
            repository.save(leccion);
        }
    }

    private void subirAudioQuiz(Lecciones leccion, Boolean forzar) {
        JsonElement jsonElement = JsonParser.parseString(leccion.getContenido());

        String path = "lecciones/" + leccion.getTipoLeccion() + "_" + leccion.getIdLeccion() + "/audios/";

        List<AudioDto> audios = new ArrayList<>();

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("audios") && !forzar) {
                log.info("Ya existen audios cargados en quizes");
                return;
            }
            JsonArray quiz = jsonObject.getAsJsonArray("quizData");

            quiz.forEach((element) -> {
                JsonObject object = element.getAsJsonObject();
                String id = object.get("id").getAsString();
                String audioPregunta = object.get("audioPregunta").getAsString();
                String fileName = "QUIZ_".concat(id);
                log.info("Id: {}, audioPregunta: {}", id, audioPregunta);
                byte[] audioBytes = textToSpeechService.sintetizarAudio(audioPregunta);

                MultipartFile multipartFile = new ByteArrayMultipartFile(audioBytes,fileName, Constantes.CONTENT_TYPE_AUDIO);
                String url = storageService.upload(multipartFile, path.concat(fileName));
                audios.add(new AudioDto(id, url));
            });

            log.info("Audios quizzes {}", audios);
            Type listType = new TypeToken<List<AudioDto>>() {}.getType();
            JsonElement audiosElement = gson.toJsonTree(audios, listType);
            jsonObject.add("audios", audiosElement);

            leccion.setContenido(gson.toJson(jsonObject));
            repository.save(leccion);
        }
    }

    private void subirAudioLecturas(Lecciones leccion, Boolean forzar) {
        JsonElement jsonElement = JsonParser.parseString(leccion.getContenido());

        String path = "lecciones/" + leccion.getTipoLeccion() + "_" + leccion.getIdLeccion() + "/audios/";

        List<AudioDto> audios = new ArrayList<>();

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("audios") && !forzar) {
                log.info("Ya existen audios cargados en lecturas");
                return;
            }
            JsonArray quiz = jsonObject.getAsJsonArray("Secciones");

            quiz.forEach((element) -> {
                JsonObject object = element.getAsJsonObject();
                String id = object.get("id").getAsString();
                String audioTexto = object.get("audioTexto").getAsString();
                String fileName = "LECTURA_".concat(id);
                log.info("Id: {}, audioTexto: {}", id, audioTexto);
                byte[] audioBytes = textToSpeechService.sintetizarAudio(audioTexto);

                MultipartFile multipartFile = new ByteArrayMultipartFile(audioBytes,fileName, Constantes.CONTENT_TYPE_AUDIO);
                String url = storageService.upload(multipartFile, path.concat(fileName));
                audios.add(new AudioDto(id, url));
            });

            log.info("Audios lecturas {}", audios);
            Type listType = new TypeToken<List<AudioDto>>() {}.getType();
            JsonElement audiosElement = gson.toJsonTree(audios, listType);
            jsonObject.add("audios", audiosElement);

            leccion.setContenido(gson.toJson(jsonObject));
            repository.save(leccion);
        }
    }

    public List<UsuarioLeccionesDto> getLeccionesByUsername(String username) {
        List<Lecciones> lecciones = repository.findAll();
        List<UsuariosLecciones> usuariosLecciones = usuariosLeccionesService.getLeccionesByUsuario(username);

        List<UsuarioLeccionesDto> response = new ArrayList<>();

        response = lecciones.stream().map(this::convertirLeccionToUsuarioLeccion).collect(Collectors.toList());

        if (!usuariosLecciones.isEmpty()) {
            for (UsuariosLecciones usuarioLeccion : usuariosLecciones) {
                response
                        .stream()
                        .filter(r -> r.getId() == usuarioLeccion.getLeccionId())
                        .findFirst()
                        .get()
                        .setCompletado(usuarioLeccion.getCompletado());
            }
        }

        return response;
    }

    public UsuarioLeccionesDto convertirLeccionToUsuarioLeccion(Lecciones leccion) {
        return new UsuarioLeccionesDto(leccion.getIdLeccion(), leccion.getTitulo(), leccion.getTipoLeccion(), false);
    }
}
