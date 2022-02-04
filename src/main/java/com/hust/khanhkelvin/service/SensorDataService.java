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

    HashMap<String, List<SensorData>> getAllValueOfLeds(Long houseId);

    /**
     * lay tat ca thong so cua thiet bi trong nha tai thoi diem moi nhat
     *
     * @param houseId
     * @return
     */
    HashMap<String, List<SensorData>> getValueEachSensorData(Long houseId);
}
