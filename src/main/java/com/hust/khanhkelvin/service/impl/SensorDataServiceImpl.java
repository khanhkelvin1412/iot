package com.hust.khanhkelvin.service.impl;

import com.hust.khanhkelvin.domain.HouseSensorEntity;
import com.hust.khanhkelvin.domain.SensorDataEntity;
import com.hust.khanhkelvin.dto.response.UserInfo;
import com.hust.khanhkelvin.dto.response.sensor.SensorData;
import com.hust.khanhkelvin.repository.HouseSensorRepository;
import com.hust.khanhkelvin.repository.SensorDataRepository;
import com.hust.khanhkelvin.repository.SensorRepository;
import com.hust.khanhkelvin.service.SensorDataService;
import com.hust.khanhkelvin.service.UserService;
import com.hust.khanhkelvin.service.mapper.SensorDataMapper;
import com.hust.khanhkelvin.utils.SensorType;
import com.hust.khanhkelvin.web.error.BadRequestAlertException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorDataMapper sensorDataMapper;

    private final UserService userService;

    private final SensorDataRepository sensorDataRepository;

    private final HouseSensorRepository houseSensorRepository;

    private final SensorRepository sensorRepository;

    public SensorDataServiceImpl(SensorDataMapper sensorDataMapper, UserService userService, SensorDataRepository sensorDataRepository, HouseSensorRepository houseSensorRepository, SensorRepository sensorRepository) {
        this.sensorDataMapper = sensorDataMapper;
        this.userService = userService;
        this.sensorDataRepository = sensorDataRepository;
        this.houseSensorRepository = houseSensorRepository;
        this.sensorRepository = sensorRepository;
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
        List<Long> houseSensorIds = houseSensorRepository.findByHouseId(houseId)
                .stream().map(HouseSensorEntity::getId)
                .collect(Collectors.toList());

        List<SensorDataEntity> sensorDataEntities = sensorDataRepository.findAllByHouseSensorIds(houseSensorIds);

        HashMap<String, List<SensorData>> response = new HashMap<>();

        // List HUMIDITY
        List<SensorDataEntity> dataHumidities = sensorDataEntities.stream()
                .filter(data -> Objects.equals(data.getSensorType(), SensorType.HUMIDITY))
                .sorted(Comparator.comparing(SensorDataEntity::getCreatedDate).reversed())
                .limit(10)
                .sorted(Comparator.comparing(SensorDataEntity::getCreatedDate))
                .collect(Collectors.toList());

        // List THERMOMETER
        List<SensorDataEntity> dataThermometer = sensorDataEntities.stream()
                .filter(data -> Objects.equals(data.getSensorType(), SensorType.THERMOMETER))
                .sorted(Comparator.comparing(SensorDataEntity::getCreatedDate).reversed())
                .limit(10)
                .sorted(Comparator.comparing(SensorDataEntity::getCreatedDate))
                .collect(Collectors.toList());

        // List GAS CONCENTRATION
        List<SensorDataEntity> dataGasConcentration = sensorDataEntities.stream()
                .filter(data -> Objects.equals(data.getSensorType(), SensorType.GAS_CONCENTRATION))
                .sorted(Comparator.comparing(SensorDataEntity::getCreatedDate).reversed())
                .limit(10)
                .sorted(Comparator.comparing(SensorDataEntity::getCreatedDate))
                .collect(Collectors.toList());

        // List door
        List<SensorDataEntity> doors = new ArrayList<>(
                sensorDataEntities.stream()
                        .filter(data -> Objects.equals(data.getSensorType(), SensorType.DOOR))
                        .collect(Collectors.toMap(
                                SensorDataEntity::getHouseSensorId,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(SensorDataEntity::getCreatedDate))))
                        .values());

        // List led
        List<SensorDataEntity> leds = new ArrayList<>(
                sensorDataEntities.stream()
                        .filter(data -> Objects.equals(data.getSensorType(), SensorType.LED))
                        .collect(Collectors.toMap(
                                SensorDataEntity::getHouseSensorId,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(SensorDataEntity::getCreatedDate))))
                        .values());

        response.put(SensorType.HUMIDITY.name(), sensorDataMapper.toDto(dataHumidities));
        response.put(SensorType.THERMOMETER.name(), sensorDataMapper.toDto(dataThermometer));
        response.put(SensorType.GAS_CONCENTRATION.name(), sensorDataMapper.toDto(dataGasConcentration));
        response.put(SensorType.DOOR.name(), sensorDataMapper.toDto(doors));
        response.put(SensorType.LED.name(), sensorDataMapper.toDto(leds));
        return response;
    }

    @Override
    public HashMap<String, List<SensorData>> getAllValueOfLeds(Long houseId) {
        List<Long> houseSensorIds = houseSensorRepository.findByHouseId(houseId)
                .stream().map(HouseSensorEntity::getId)
                .collect(Collectors.toList());

        List<SensorDataEntity> sensorDataEntities = sensorDataRepository.findAllByHouseSensorIds(houseSensorIds);

        HashMap<String, List<SensorData>> response = new HashMap<>();

        // List led
        List<SensorDataEntity> dataLeds = sensorDataEntities.stream()
                .filter(data -> Objects.equals(data.getSensorType(), SensorType.LED))
                .limit(10)
                .collect(Collectors.toList());

        response.put(SensorType.LED.name(), sensorDataMapper.toDto(dataLeds));
        return response;
    }

    @Override
    public HashMap<String, List<SensorData>> getValueEachSensorData(Long houseId) {
        List<Long> houseSensorIds = houseSensorRepository.findByHouseId(houseId)
                .stream().map(HouseSensorEntity::getId)
                .collect(Collectors.toList());

        HashMap<String, List<SensorData>> response = new HashMap<>();

        List<SensorDataEntity> sensorDataEntities = sensorDataRepository.findAllByHouseSensorIds(houseSensorIds);

        List<SensorDataEntity> dataHumidities = new ArrayList<>(
                sensorDataEntities.stream()
                        .filter(data -> Objects.equals(data.getSensorType(), SensorType.HUMIDITY))
                        .collect(Collectors.toMap(
                                SensorDataEntity::getHouseSensorId,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(SensorDataEntity::getCreatedDate))))
                        .values());

        List<SensorDataEntity> dataThermometer = new ArrayList<>(
                sensorDataEntities.stream()
                        .filter(data -> Objects.equals(data.getSensorType(), SensorType.THERMOMETER))
                        .collect(Collectors.toMap(
                                SensorDataEntity::getHouseSensorId,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(SensorDataEntity::getCreatedDate))))
                        .values());

        List<SensorDataEntity> dataGasConcentration = new ArrayList<>(
                sensorDataEntities.stream()
                        .filter(data -> Objects.equals(data.getSensorType(), SensorType.GAS_CONCENTRATION))
                        .collect(Collectors.toMap(
                                SensorDataEntity::getHouseSensorId,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(SensorDataEntity::getCreatedDate))))
                        .values());

        List<SensorDataEntity> doors = new ArrayList<>(
                sensorDataEntities.stream()
                        .filter(data -> Objects.equals(data.getSensorType(), SensorType.DOOR))
                        .collect(Collectors.toMap(
                                SensorDataEntity::getHouseSensorId,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(SensorDataEntity::getCreatedDate))))
                        .values());

        List<SensorDataEntity> leds = new ArrayList<>(
                sensorDataEntities.stream()
                        .filter(data -> Objects.equals(data.getSensorType(), SensorType.LED))
                        .collect(Collectors.toMap(
                                SensorDataEntity::getHouseSensorId,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(SensorDataEntity::getCreatedDate))))
                        .values());

        response.put(SensorType.HUMIDITY.name(), sensorDataMapper.toDto(dataHumidities));
        response.put(SensorType.THERMOMETER.name(), sensorDataMapper.toDto(dataThermometer));
        response.put(SensorType.GAS_CONCENTRATION.name(), sensorDataMapper.toDto(dataGasConcentration));
        response.put(SensorType.DOOR.name(), sensorDataMapper.toDto(doors));
        response.put(SensorType.LED.name(), sensorDataMapper.toDto(leds));
        return response;
    }
}
