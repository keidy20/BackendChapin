package com.app.chapin.services;

import com.app.chapin.persistence.dtos.response.SpeechToTextResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Base64;

@Slf4j
@Service
public class SpeechToTextService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${GOOGLE_API_KEY}")
    private String apiKey;

    @Value("${GOOGLE_URL_SPEACH_TO_TEXT}")
    private String urlSpeachToText;


    public String encodeFileToBase64(MultipartFile file) throws IOException {
        // Tamaño original del archivo en bytes
        long originalSize = file.getSize();
        System.out.println("Tamaño original del archivo (bytes): " + originalSize);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(file.getBytes());
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String encodeFileToBase642(MultipartFile file) throws IOException {
        // Tamaño original del archivo en bytes
        long originalSize = file.getSize();
        System.out.println("Tamaño original del archivo (bytes): " + originalSize);

        // Leer el archivo en bytes
        byte[] bytes = file.getBytes();

        // Codificar en Base64
        String base64Encoded = Base64.getEncoder().encodeToString(bytes);

        // Tamaño de la cadena Base64
        int base64Length = base64Encoded.length();
        System.out.println("Tamaño de la cadena Base64 (caracteres): " + base64Length);

        return base64Encoded;
    }

    public String transcribeAudio(String base64Audio) throws IOException {

        try {

            String requestBody = """
            {
                "config": {
                    "encoding": "MP3",
                    "sampleRateHertz": 8000,
                    "model": "default",
                    "languageCode": "es-GT"
                },
                "audio": {
                    "content": "%s"
                }
            }
            """.formatted(base64Audio);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(urlSpeachToText.concat(apiKey), entity, String.class);

            String responseBody = response.getBody();

            JsonElement jsonElement = JsonParser.parseString(responseBody);
            // Verificar si el JSON es un objeto
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                // Navegar por el JSON para obtener el valor de "transcript"
                JsonArray resultsArray = jsonObject.getAsJsonArray("results");
                if (resultsArray.size() > 0) {
                    JsonObject firstResult = resultsArray.get(0).getAsJsonObject();
                    JsonArray alternativesArray = firstResult.getAsJsonArray("alternatives");
                    if (alternativesArray.size() > 0) {
                        JsonObject firstAlternative = alternativesArray.get(0).getAsJsonObject();
                        String transcript = firstAlternative.get("transcript").getAsString();

                        // Mostrar el valor de "transcript"
                        log.info("Transcript {}", transcript);
                        return transcript;
                    }
                }
            }

            return "";
        } catch (Exception e) {
            throw e;
        }
    }

    public SpeechToTextResponse compararTextos(String texto, String transcription) {

        String texto1Normalize = normalizeAndRemovePunctuation(texto);
        String texto2Normalize = normalizeAndRemovePunctuation(transcription);
        int cantidadPalabras = texto2Normalize.split(" ").length;

        log.info("Texto 1 {}", texto1Normalize);
        log.info("Texto 2 {}", texto2Normalize);

        // Calcula el porcentaje de similitud
        double similitud = calcularPorcentajeSimilitud(texto1Normalize, texto2Normalize);

        log.info("Porcentaje de similitud: {}%", similitud);

        boolean textosIguales = false;
        if (similitud > 80) {
            textosIguales = true;
        }

        return new SpeechToTextResponse(similitud, textosIguales, cantidadPalabras);
    }

    private static String normalizeAndRemovePunctuation(String text) {
        // Normaliza el texto para descomponer caracteres acentuados
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        // Elimina los signos de puntuación
        normalized = normalized.replaceAll("[\\p{Punct}]", "");

        // Elimina las tildes de los caracteres acentuados
        normalized = normalized.replaceAll("\\p{M}", ""); // Elimina marcas diacríticas (tildes)

        // Opcionalmente, puedes eliminar espacios adicionales
        normalized = normalized.replaceAll("\\s+", " ").trim();

        // Convierte a minúsculas para hacer la comparación insensible a mayúsculas/minúsculas
        return normalized.toLowerCase();
    }

    private static double calcularPorcentajeSimilitud(String text1, String text2) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

        // Calcula la distancia de Levenshtein entre los dos textos
        int distance = levenshteinDistance.apply(text1, text2);

        // Calcula la longitud del texto más largo
        int maxLength = Math.max(text1.length(), text2.length());

        // Calcula el porcentaje de similitud
        double similarity = (1.0 - (double) distance / maxLength) * 100;

        return similarity;
    }
}
