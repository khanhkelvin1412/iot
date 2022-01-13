package com.hust.khanhkelvin.service;

import com.hust.khanhkelvin.dto.response.sensor.SensorData;

import java.util.HashMap;
import java.util.List;

public interface SensorDataService {
    /**
     * save sensor data from broker
     *
     * @param sensorData
     * @return
     */
    Long saveSensorData(SensorData sensorData);

    /**
     * get all value of sensor in house
     *
     * @param houseId
     * @return
     */
    HashMap<String, List<SensorData>> getAllValueOfSensors(Long houseId);

}
