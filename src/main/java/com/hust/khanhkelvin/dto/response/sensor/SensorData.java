package com.hust.khanhkelvin.dto.response.sensor;

import com.hust.khanhkelvin.utils.SensorType;
import lombok.Data;

@Data
public class SensorData {

    private Long sensorId;

    private Long houseId;

    private SensorType sensorType;

    private boolean status;

    private Integer temp;

    private Integer hum;

    private Double gasConcentration;
}
