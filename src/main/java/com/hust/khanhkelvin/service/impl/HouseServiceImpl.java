package com.hust.khanhkelvin.service.impl;

import com.hust.khanhkelvin.domain.HouseEntity;
import com.hust.khanhkelvin.domain.HouseSensorEntity;
import com.hust.khanhkelvin.domain.SensorEntity;
import com.hust.khanhkelvin.domain.UserEntity;
import com.hust.khanhkelvin.dto.request.HouseInfoRequest;
import com.hust.khanhkelvin.dto.response.house.HouseInfoDetail;
import com.hust.khanhkelvin.dto.response.house.HouseInfoInsert;
import com.hust.khanhkelvin.dto.response.sensor.SensorDetail;
import com.hust.khanhkelvin.repository.HouseRepository;
import com.hust.khanhkelvin.repository.HouseSensorRepository;
import com.hust.khanhkelvin.repository.SensorRepository;
import com.hust.khanhkelvin.repository.UserRepository;
import com.hust.khanhkelvin.security.SecurityUtils;
import com.hust.khanhkelvin.service.HouseSensorService;
import com.hust.khanhkelvin.service.HouseService;
import com.hust.khanhkelvin.service.mapper.HouseInfoDetailMapper;
import com.hust.khanhkelvin.service.mapper.HouseInfoMapper;
import com.hust.khanhkelvin.service.mapper.SensorMapper;
import com.hust.khanhkelvin.utils.RandomUtil;
import com.hust.khanhkelvin.web.error.BadRequestAlertException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {

    private final UserRepository userRepository;

    private final HouseRepository houseRepository;

    private final HouseInfoMapper houseInfoMapper;

    private final HouseSensorService houseSensorService;

    private final HouseInfoDetailMapper houseInfoDetailMapper;

    private final SensorMapper sensorMapper;

    private final HouseSensorRepository houseSensorRepository;

    private final SensorRepository sensorRepository;

    public HouseServiceImpl(
            UserRepository userRepository,
            HouseInfoMapper houseInfoMapper,
            HouseRepository houseRepository,
            HouseSensorService houseSensorService,
            HouseInfoDetailMapper houseInfoDetailMapper,
            SensorMapper sensorMapper,
            HouseSensorRepository houseSensorRepository,
            SensorRepository sensorRepository
        ) {

        this.userRepository = userRepository;
        this.houseRepository = houseRepository;
        this.houseInfoMapper = houseInfoMapper;
        this.houseSensorService = houseSensorService;
        this.houseInfoDetailMapper = houseInfoDetailMapper;
        this.sensorMapper = sensorMapper;
        this.houseSensorRepository = houseSensorRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public List<HouseInfoDetail> getHouses() {
        UserEntity user = getUser();
        if(user == null) return null;

        Set<HouseEntity> houses = houseRepository.findByUserId(user.getId()).orElse(null);
        if(houses == null) return null;

        List<SensorEntity> allSensorEntities = sensorRepository.findAll();
        Map<Long, SensorEntity> sensorEntityMap = new HashMap<>();
        for(SensorEntity sensorEntity: allSensorEntities){
            sensorEntityMap.put(sensorEntity.getId(), sensorEntity);
        }

        List<HouseInfoDetail> houseInfoDetails = houseInfoDetailMapper.toDto(new ArrayList<>(houses));

        for(HouseInfoDetail houseInfoDetail: houseInfoDetails){
            Set<HouseSensorEntity> houseSensorEntities = houseSensorRepository.
                            findHouseSensorByHouseID(houseInfoDetail.getId()).
                            orElse(null);
            if(houseSensorEntities == null) return null;

            List<SensorDetail> sensorDetails = new ArrayList<>();
            for(HouseSensorEntity houseSensorEntity : houseSensorEntities){
                SensorEntity sensorEntity = sensorEntityMap.get(houseSensorEntity.getSensorId());
                SensorDetail sensorDetail = sensorMapper.toDto(sensorEntity);
                sensorDetail.setIdx(houseSensorEntity.getId());
                sensorDetails.add(sensorDetail);
            }
            houseInfoDetail.setSensors(sensorDetails);
        }

        return houseInfoDetails;
    }

    @Override
    @Transactional
    public HouseInfoInsert insertHouse(HouseInfoRequest houseInfoRequest) {
        UserEntity user = getUser();
        if(user == null) return null;

        if(existHouse(houseInfoRequest.getName(), user.getId())){
            throw new BadRequestAlertException("House name was used " , "houseNameWasUsed");
        }

        if(houseInfoRequest.getSensors().size() == 0){
            throw new BadRequestAlertException("House must have device " , "houseMustHaveDevice");
        }

        // save house
        HouseEntity houseEntity = new HouseEntity();
        houseEntity.setName(houseInfoRequest.getName());
        houseEntity.setCode(RandomUtil.genCodeHouse());
        houseEntity.setStatus(1);
        houseEntity.setUser(user);
        HouseEntity houseEntityAfterSave = houseRepository.save(houseEntity);

        // save sensor
        int success = houseSensorService.saveAll(
                houseInfoRequest.getSensors(),
                houseEntityAfterSave
            );

        HouseInfoInsert houseInfoInsert = houseInfoMapper.toDto(houseEntity);
        houseInfoInsert.setSuccess(success);
        return houseInfoInsert;
    }

    @Override
    public boolean existHouse(String name, Long userID) {
        return houseRepository.existHouse(name, userID).isPresent();
    }

    private UserEntity getUser(){
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (!currentUserLogin.isPresent()) return null;
        String currentUser = currentUserLogin.get();
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(currentUser);
        return userEntityOptional.orElse(null);
    }
}
