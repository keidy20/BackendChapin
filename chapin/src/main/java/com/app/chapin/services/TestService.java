package com.app.chapin.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestService {

    private String pingUrlSatGestor = "https://rtu.desa.sat.gob.gt/api/sat-gestor-app";

    public ResponseEntity<byte[]> obtenerArchivo(String idArchivo) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename.pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        try {
            String urlProxy = this.pingUrlSatGestor.concat("/process/files/").concat(idArchivo);
            byte[] response = restTemplate.exchange(urlProxy, HttpMethod.GET, requestEntity, byte[].class).getBody();

            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error al obtener el archivo");
            throw  e;
        }
    }
}
