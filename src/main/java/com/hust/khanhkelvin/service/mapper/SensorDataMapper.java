package com.hust.khanhkelvin.service.mapper;

import com.hust.khanhkelvin.domain.SensorDataEntity;
import com.hust.khanhkelvin.dto.response.sensor.SensorData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface SensorDataMapper extends EntityMapper<SensorData, SensorDataEntity> {
    @Mappings({
            @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "toCreatedDate"),
            @Mapping(source = "lastModifiedDate", target = "lastModifiedDate", qualifiedByName = "toCreatedDate")
    })
    SensorData toDto(SensorDataEntity sensorDataEntity);

    @Named("toCreatedDate")
    default String dateToString(Instant createdDate) {
        return createdDate.toString();
    }
}
