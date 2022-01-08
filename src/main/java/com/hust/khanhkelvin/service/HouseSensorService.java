package com.hust.khanhkelvin.service;

import com.hust.khanhkelvin.domain.HouseEntity;
import com.hust.khanhkelvin.domain.HouseSensorEntity;
import com.hust.khanhkelvin.dto.request.SensorInfoRequest;

import java.util.List;

public interface HouseSensorService {
    int save(SensorInfoRequest sensorInfoRequest, HouseEntity house);
    int saveAll(List<SensorInfoRequest> sensorInfoRequests, HouseEntity house);
}
