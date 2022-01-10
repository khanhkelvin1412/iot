package com.hust.khanhkelvin.dto.response.house;

import com.hust.khanhkelvin.dto.response.sensor.SensorDetail;
import lombok.Data;

import java.util.List;

@Data
public class HouseInfoDetail extends BaseHouseInfo{
    List<SensorDetail> sensors;
}
