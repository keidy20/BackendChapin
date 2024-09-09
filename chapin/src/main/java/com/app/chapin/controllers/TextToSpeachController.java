package com.app.chapin.controllers;

import com.app.chapin.persistence.dtos.request.ParamsApiTextToSpeechGoogleDto;
import com.app.chapin.services.TextToSpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/text-to-speach")
public class TextToSpeachController {

    @Autowired
    private TextToSpeechService service;

    @PostMapping
    public ResponseEntity<ByteArrayResource> sintetizarAudio(@RequestBody ParamsApiTextToSpeechGoogleDto dto) {
        try {
            byte[] audioBytes = service.sintetizarAudio(dto.getTexto());

            ByteArrayResource resource = new ByteArrayResource(audioBytes);

            // Configurar encabezados para la descarga
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.mp3");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
