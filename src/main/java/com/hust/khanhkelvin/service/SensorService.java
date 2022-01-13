package com.hust.khanhkelvin.service;

import com.hust.khanhkelvin.dto.response.sensor.SensorDetail;
import sun.management.Sensor;

import java.util.List;

public interface SensorService {
    List<SensorDetail> getSensors();
}
