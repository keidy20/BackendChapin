package com.app.chapin.persistence.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeechToTextResponse {
    private double similitud;
    private boolean igual;
    private int cantidadPalabras;
}
