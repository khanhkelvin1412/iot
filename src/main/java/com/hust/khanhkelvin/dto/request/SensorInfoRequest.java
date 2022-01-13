package com.hust.khanhkelvin.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorInfoRequest {
    @NotBlank
    private Long id;

    @NotBlank
    private Integer quantity;
}
