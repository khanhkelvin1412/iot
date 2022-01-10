package com.hust.khanhkelvin.service.mapper;

import com.hust.khanhkelvin.domain.HouseEntity;
import com.hust.khanhkelvin.dto.response.house.HouseInfoDetail;
import com.hust.khanhkelvin.dto.response.house.HouseInfoInsert;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HouseInfoDetailMapper extends EntityMapper<HouseInfoDetail, HouseEntity>{
}
