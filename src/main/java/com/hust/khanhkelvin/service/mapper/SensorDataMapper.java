package com.hust.khanhkelvin.service.mapper;

import com.hust.khanhkelvin.domain.SensorDataEntity;
import com.hust.khanhkelvin.dto.response.sensor.SensorData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SensorDataMapper extends EntityMapper<SensorData, SensorDataEntity> {
}
