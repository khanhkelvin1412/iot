package com.hust.khanhkelvin.web.rest;

import com.hust.khanhkelvin.dto.response.sensor.SensorData;
import com.hust.khanhkelvin.dto.response.sensor.SensorDetail;
import com.hust.khanhkelvin.service.SensorDataService;
import com.hust.khanhkelvin.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.management.Sensor;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class SensorDataResource {

    private final SensorDataService sensorDataService;

    private final SensorService sensorService;

    public SensorDataResource(SensorDataService sensorDataService, SensorService sensorService) {
        this.sensorDataService = sensorDataService;
        this.sensorService = sensorService;
    }

    @GetMapping("/sensors")
    public ResponseEntity<List<SensorDetail>> getSensors() {
        return ResponseEntity.ok(sensorService.getSensors());
    }

    @PostMapping("/sensor-data-house/sensor/{houseId}")
    public ResponseEntity<HashMap<String, List<SensorData>>> getAllValueOfSensors(@PathVariable Long houseId) {
        HashMap<String, List<SensorData>> response = sensorDataService.getAllValueOfSensors(houseId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sensor-data-house/led/{houseId}")
    public ResponseEntity<HashMap<String, List<SensorData>>> getAllValueOfLeds(@PathVariable Long houseId) {
        HashMap<String, List<SensorData>> response = sensorDataService.getAllValueOfLeds(houseId);
        return ResponseEntity.ok(response);
    }
}
