package com.hust.khanhkelvin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.khanhkelvin.config.RabbitMQConfigurationProperties;
import com.hust.khanhkelvin.domain.HouseEntity;
import com.hust.khanhkelvin.domain.HouseSensorEntity;
import com.hust.khanhkelvin.domain.SensorDataEntity;
import com.hust.khanhkelvin.domain.SensorEntity;
import com.hust.khanhkelvin.dto.request.SensorInfoRequest;
import com.hust.khanhkelvin.dto.response.house_sensor.HouseSensorData;
import com.hust.khanhkelvin.repository.HouseSensorRepository;
import com.hust.khanhkelvin.repository.SensorDataRepository;
import com.hust.khanhkelvin.repository.SensorRepository;
import com.hust.khanhkelvin.service.HouseSensorService;
import com.hust.khanhkelvin.utils.SensorType;
import com.hust.khanhkelvin.web.error.BadRequestAlertException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class HouseSensorServiceImpl implements HouseSensorService {

    private final RabbitMQConfigurationProperties properties;

    private final ObjectMapper objectMapper;

    private static final String routingKey = "queue.hust.iot.house-sensor-key";

    private static final String exchange = "house-sensor";

    private final RabbitTemplate rabbitTemplate;

    private final SensorDataRepository sensorDataRepository;

    private final HouseSensorRepository houseSensorRepository;

    private final SensorRepository sensorRepository;

    public HouseSensorServiceImpl(
            RabbitMQConfigurationProperties properties, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate, SensorDataRepository sensorDataRepository, HouseSensorRepository houseSensorRepository,
            SensorRepository sensorRepository
    ) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.sensorDataRepository = sensorDataRepository;

        this.houseSensorRepository = houseSensorRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    @Transactional
    public int save(SensorInfoRequest sensorInfoRequest, HouseEntity house) {
        Long id = sensorInfoRequest.getId();
        Optional<SensorEntity> sensorEntityOptional = sensorRepository.findById(id);
        if(!sensorEntityOptional.isPresent())
            throw new BadRequestAlertException(
                    String.format("Sensor id %s not found!", id),
                    "sensorNotFound"
            );
        SensorEntity sensorEntity = sensorEntityOptional.get();

        List<HouseSensorEntity> sensors = new ArrayList<>();
        for(int i = 0; i < sensorInfoRequest.getQuantity(); i++){
            HouseSensorEntity houseSensorEntity = new HouseSensorEntity();
            houseSensorEntity.setHouseId(house.getId());
            houseSensorEntity.setSensorId(sensorEntity.getId());
            sensors.add(houseSensorEntity);
        }

        houseSensorRepository.saveAll(sensors);
        return sensorInfoRequest.getQuantity();
    }

    @Override
    @Transactional
    public int saveAll(List<SensorInfoRequest> sensorInfoRequests, HouseEntity house) {
        List<HouseSensorEntity> sensors = new ArrayList<>();
        int success = 0;

        for(SensorInfoRequest sensorInfoRequest: sensorInfoRequests){
            Long id = sensorInfoRequest.getId();
            Optional<SensorEntity> sensorEntityOptional = sensorRepository.findById(id);
            if(!sensorEntityOptional.isPresent())
                throw new BadRequestAlertException(
                        String.format("Sensor id %s not found!", id),
                        "sensorNotFound"
                );
            SensorEntity sensorEntity = sensorEntityOptional.get();

            for(int i = 0; i < sensorInfoRequest.getQuantity(); i++){
                HouseSensorEntity houseSensorEntity = new HouseSensorEntity();
                houseSensorEntity.setHouseId(house.getId());
                houseSensorEntity.setSensorId(sensorEntity.getId());
                sensors.add(houseSensorEntity);
            }

            success += sensorInfoRequest.getQuantity();
        }

        // list sensor to map type
        List<SensorEntity> sensorEntities = sensorRepository.findAll();
        List<HouseSensorEntity> houseSensorSavedList = houseSensorRepository.saveAll(sensors);
        houseSensorSavedList.forEach(item -> {
            Optional<SensorEntity> optionalSensorEntity = sensorEntities.stream()
                    .filter(sensorE -> Objects.equals(sensorE.getId(), item.getSensorId()))
                    .findFirst();
            if (optionalSensorEntity.isPresent()) {
                if (SensorType.LED.equals(optionalSensorEntity.get().getType())) {
                    SensorDataEntity sensorDataEntity = new SensorDataEntity();
                    sensorDataEntity.setSensorType(SensorType.LED);
                    sensorDataEntity.setStatus(false);
                    sensorDataEntity.setHouseSensorId(item.getId());
                    sensorDataRepository.save(sensorDataEntity);
                } else if (SensorType.DOOR.equals(optionalSensorEntity.get().getType())) {
                    SensorDataEntity sensorDataEntity = new SensorDataEntity();
                    sensorDataEntity.setSensorType(SensorType.DOOR);
                    sensorDataEntity.setStatus(false);
                    sensorDataEntity.setHouseSensorId(item.getId());
                    sensorDataRepository.save(sensorDataEntity);
                }
                HouseSensorData houseSensorData = new HouseSensorData(item.getId(), house.getName(), item.getId(), optionalSensorEntity.get().getType());

                // send infor to rabbitmq
                try {
                    rabbitTemplate.convertAndSend(
                            properties.getHouseSensorExchange(),
                            properties.getHouseSensorRoutingKey(),
                            objectMapper.writeValueAsString(houseSensorData));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return success;
    }
}
