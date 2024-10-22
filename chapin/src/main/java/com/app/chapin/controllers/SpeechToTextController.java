package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.ParamsApiSpeechToTextGoogleDto;
import com.app.chapin.services.SpeechToTextService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

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
    @PostMapping(value = "/compare-by-audio-2", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile2(@RequestParam("file") MultipartFile file, @RequestParam("texto") String texto) {
        try {
            // Codificar el archivo en Base64
            String base64Audio = service.encodeFileToBase64(file);

            // Transcribir el audio usando la API de Google Speech-to-Text
            //String transcription = service.transcribeAudio(base64Audio);

            //return ResponseEntity.ok(service.compararTextos(texto, transcription));

            return ResponseEntity.ok("");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrio un error al comparar los textos");
        }
    }

    @PostMapping("/transcribir-base-64")
    public ResponseEntity<?> transcribir(@RequestBody ParamsApiSpeechToTextGoogleDto dto) {
        try {

            // Transcribir el audio usando la API de Google Speech-to-Text
            String transcription = service.transcribeAudio(dto.getBase64());
            return ResponseEntity.ok(service.compararTextos(dto.getTexto(), transcription));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrio un error al comparar los textos");
        }
    }



}
