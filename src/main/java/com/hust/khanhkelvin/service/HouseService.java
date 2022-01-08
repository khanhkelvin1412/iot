package com.hust.khanhkelvin.service;

import com.hust.khanhkelvin.dto.request.HouseInfoRequest;
import com.hust.khanhkelvin.dto.response.house.HouseInfoDetail;
import com.hust.khanhkelvin.dto.response.house.HouseInfoInsert;

import java.util.List;

public interface HouseService {
    List<HouseInfoDetail> getHouses();

    HouseInfoInsert insertHouse(HouseInfoRequest houseInfoRequest);

    boolean existHouse(String name, Long userID);
}
