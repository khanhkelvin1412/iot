package com.hust.khanhkelvin.service.impl;

import com.hust.khanhkelvin.domain.HouseSensorEntity;
import com.hust.khanhkelvin.domain.SensorDataEntity;
import com.hust.khanhkelvin.dto.response.UserInfo;
import com.hust.khanhkelvin.dto.response.sensor.SensorData;
import com.hust.khanhkelvin.repository.HouseSensorRepository;
import com.hust.khanhkelvin.repository.SensorDataRepository;
import com.hust.khanhkelvin.service.SensorDataService;
import com.hust.khanhkelvin.service.UserService;
import com.hust.khanhkelvin.service.mapper.SensorDataMapper;
import com.hust.khanhkelvin.utils.SensorType;
import com.hust.khanhkelvin.web.error.BadRequestAlertException;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorDataMapper sensorDataMapper;

    private final UserService userService;

    private final SensorDataRepository sensorDataRepository;

    private final HouseSensorRepository houseSensorRepository;

    public SensorDataServiceImpl(SensorDataMapper sensorDataMapper, UserService userService, SensorDataRepository sensorDataRepository, HouseSensorRepository houseSensorRepository) {
        this.sensorDataMapper = sensorDataMapper;
        this.userService = userService;
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

    @Override
    public HashMap<String, List<SensorData>> getAllValueOfSensors(Long houseId) {
        UserInfo user = userService.getCurrentUser();
        List<Long> houseSensorIds = houseSensorRepository.findByHouseId(houseId)
                .stream().map(HouseSensorEntity::getId)
                .collect(Collectors.toList());

        List<SensorDataEntity> sensorDataEntities = sensorDataRepository.findAllByHouseSensorIds(houseSensorIds);

        HashMap<String, List<SensorData>> response = new HashMap<>();

        // List HUMIDITY
        List<SensorDataEntity> dataHumidities = sensorDataEntities.stream()
                .filter(data -> Objects.equals(data.getSensorType(), SensorType.HUMIDITY))
                .limit(10)
                .collect(Collectors.toList());

        // List THERMOMETER
        List<SensorDataEntity> dataThermoeter = sensorDataEntities.stream()
                .filter(data -> Objects.equals(data.getSensorType(), SensorType.THERMOMETER))
                .limit(10)
                .collect(Collectors.toList());

        // List GAS CONCENTRATION
        List<SensorDataEntity> dataGasConcentration = sensorDataEntities.stream()
                .filter(data -> Objects.equals(data.getSensorType(), SensorType.GAS_CONCENTRATION))
                .limit(10)
                .collect(Collectors.toList());

        // List door

        // List led

        response.put(SensorType.HUMIDITY.name(), sensorDataMapper.toDto(dataHumidities));
        response.put(SensorType.THERMOMETER.name(), sensorDataMapper.toDto(dataThermoeter));
        response.put(SensorType.GAS_CONCENTRATION.name(), sensorDataMapper.toDto(dataGasConcentration));
        return response;
    }
}
