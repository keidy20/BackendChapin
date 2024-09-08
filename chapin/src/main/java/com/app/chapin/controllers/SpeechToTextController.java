package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.ParamsApiGoogleDto;
import com.app.chapin.services.SpeechToTextService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/speach-to-text")
public class SpeechToTextController {

    @Autowired
    private SpeechToTextService service;

    @PostMapping("/compare-by-audio")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("texto") String texto) {
        try {
            // Codificar el archivo en Base64
            String base64Audio = service.encodeFileToBase64(file);

            // Transcribir el audio usando la API de Google Speech-to-Text
            String transcription = service.transcribeAudio(base64Audio);

            return ResponseEntity.ok(service.compararTextos(texto, transcription));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrio un error al comparar los textos");
        }
    }

    @PostMapping("/transcribir-base-64")
    public String transcribir(@RequestBody ParamsApiGoogleDto dto) {
        try {

            // Transcribir el audio usando la API de Google Speech-to-Text
            String transcription = service.transcribeAudio(dto.getTexto());
            return transcription;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
