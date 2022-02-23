package com.hust.khanhkelvin.dto.response.house_sensor;

import com.hust.khanhkelvin.utils.SensorType;
import lombok.Data;

@Data
public class HouseSensorData {
    private Long houseId;

    private String houseName;

    private Long sensorId;

    private SensorType sensorType;

    public HouseSensorData(Long houseId, String houseName, Long sensorId, SensorType sensorType) {
        this.houseId = houseId;
        this.houseName = houseName;
        this.sensorId = sensorId;
        this.sensorType = sensorType;
    }
}
