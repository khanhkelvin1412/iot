package com.hust.khanhkelvin.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class HouseInfoRequest {

    @NotBlank
    private String name;

    @NotNull
    private List<SensorInfoRequest> sensors;
}
