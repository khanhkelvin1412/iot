package com.hust.khanhkelvin.service.impl;

import com.hust.khanhkelvin.dto.response.sensor.SensorDetail;
import com.hust.khanhkelvin.repository.SensorRepository;
import com.hust.khanhkelvin.service.SensorService;
import com.hust.khanhkelvin.service.mapper.SensorDetailMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;

    private final SensorDetailMapper sensorDetailMapper;

    public SensorServiceImpl(SensorRepository sensorRepository, SensorDetailMapper sensorDetailMapper) {
        this.sensorRepository = sensorRepository;
        this.sensorDetailMapper = sensorDetailMapper;
    }

    @Override
    public List<SensorDetail> getSensors() {
        return sensorDetailMapper.toDto(sensorRepository.findAll());
    }
}
