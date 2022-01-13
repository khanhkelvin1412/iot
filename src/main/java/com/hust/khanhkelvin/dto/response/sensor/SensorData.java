package com.hust.khanhkelvin.dto.response.sensor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hust.khanhkelvin.utils.SensorType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorData {

    private Long houseSensorId;

    private SensorType sensorType;

    private boolean status;

    private Integer temp;

    private Integer hum;

    private Double gasConcentration;
}
