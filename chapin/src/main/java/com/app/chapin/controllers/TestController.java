package com.app.chapin.controllers;

import com.app.chapin.services.TestService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/gestorProceso/obtenerArchivo/{idArchivo}")
    public ResponseEntity<?> getArchiviAps(@PathVariable @Parameter(description  = "id archivo") String idArchivo) {
        try {
            return testService.obtenerArchivo(idArchivo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
