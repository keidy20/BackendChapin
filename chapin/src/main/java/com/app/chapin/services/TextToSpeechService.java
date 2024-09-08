package com.app.chapin.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Slf4j
@Service
public class TextToSpeechService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final Gson gson = new Gson();

    @Value("${GOOGLE_API_KEY}")
    private String apiKey;

    @Value("${GOOGLE_URL_TEXT_TO_SPEACH}")
    private String urlTextToSpeach;

    private final String AUDIO_ENCODING = "MP3";


    public byte[] sintetizarAudio(String texto) {

        String url = urlTextToSpeach.concat(apiKey);
        String requestBody = """
            {
                "audioConfig": {
                    "audioEncoding": "%s",
                    "effectsProfileId": [
                        "small-bluetooth-speaker-class-device"
                    ],
                    "pitch": 0,
                    "speakingRate": 1
                },
                "input": {
                    "text": "%s"
                },
                "voice": {
                    "languageCode": "es-US",
                    "name": "es-US-Neural2-B"
                }
            }
            """.formatted(AUDIO_ENCODING, texto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);

        String audioContent = String.valueOf(jsonObject.get("audioContent"));
        System.out.println("Audio content " + audioContent);

        audioContent = audioContent.replaceAll("\"", "");
        byte[] audioBytes = Base64.getDecoder().decode(audioContent);
        return audioBytes;

    }
}
