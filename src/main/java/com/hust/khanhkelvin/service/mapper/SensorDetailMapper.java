package com.hust.khanhkelvin.service.mapper;

import com.hust.khanhkelvin.domain.SensorEntity;
import com.hust.khanhkelvin.dto.response.sensor.SensorDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SensorDetailMapper extends EntityMapper<SensorDetail, SensorEntity>{
}
