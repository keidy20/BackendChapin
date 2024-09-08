package com.app.chapin.persistence.dtos.request;

import lombok.Data;

@Data
public class ParamsApiSpeechToTextGoogleDto {
    private String base64;
    private String texto;
}
