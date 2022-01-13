package com.hust.khanhkelvin.service.impl;

import com.hust.khanhkelvin.domain.SensorDataEntity;
import com.hust.khanhkelvin.dto.response.sensor.SensorData;
import com.hust.khanhkelvin.repository.HouseSensorRepository;
import com.hust.khanhkelvin.repository.SensorDataRepository;
import com.hust.khanhkelvin.service.SensorDataService;
import com.hust.khanhkelvin.service.mapper.SensorDataMapper;
import com.hust.khanhkelvin.web.error.BadRequestAlertException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorDataMapper sensorDataMapper;

    private final SensorDataRepository sensorDataRepository;

    private final HouseSensorRepository houseSensorRepository;

    public SensorDataServiceImpl(SensorDataMapper sensorDataMapper, SensorDataRepository sensorDataRepository, HouseSensorRepository houseSensorRepository) {
        this.sensorDataMapper = sensorDataMapper;
        this.sensorDataRepository = sensorDataRepository;
        this.houseSensorRepository = houseSensorRepository;
    }

    @Override
    public Long saveSensorData(SensorData sensorData) {
        SensorDataEntity sensorDataEntity = sensorDataMapper.toEntity(sensorData);
        if (Objects.isNull(sensorData.getHouseSensorId())) {
            throw new BadRequestAlertException("House sensor data id must not be null", "houseSensorIdNotNull");
        }
        Boolean existedHouseSensorById = houseSensorRepository.existsById(sensorData.getHouseSensorId());
        if (Boolean.FALSE.equals(existedHouseSensorById)) {
            throw new BadRequestAlertException("Sensor of House not existed", "sensorHouseNotExited");
        } else {
            SensorDataEntity sensorDataEntityAfterSave = sensorDataRepository.save(sensorDataEntity);
            return sensorDataEntityAfterSave.getId();
        }
    }
}
