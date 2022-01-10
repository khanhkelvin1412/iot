package com.hust.khanhkelvin.service.impl;

import com.hust.khanhkelvin.domain.HouseEntity;
import com.hust.khanhkelvin.domain.HouseSensorEntity;
import com.hust.khanhkelvin.domain.SensorEntity;
import com.hust.khanhkelvin.dto.request.SensorInfoRequest;
import com.hust.khanhkelvin.repository.HouseRepository;
import com.hust.khanhkelvin.repository.HouseSensorRepository;
import com.hust.khanhkelvin.repository.SensorRepository;
import com.hust.khanhkelvin.repository.UserRepository;
import com.hust.khanhkelvin.service.HouseSensorService;
import com.hust.khanhkelvin.service.mapper.HouseInfoMapper;
import com.hust.khanhkelvin.web.error.BadRequestAlertException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HouseSensorServiceImpl implements HouseSensorService {

    private final HouseSensorRepository houseSensorRepository;

    private final SensorRepository sensorRepository;

    public HouseSensorServiceImpl(
            HouseSensorRepository houseSensorRepository,
            SensorRepository sensorRepository
        ) {

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

        houseSensorRepository.saveAll(sensors);
        return success;
    }
}
