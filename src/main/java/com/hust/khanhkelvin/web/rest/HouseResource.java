package com.hust.khanhkelvin.web.rest;

import com.hust.khanhkelvin.dto.request.HouseInfoRequest;
import com.hust.khanhkelvin.dto.response.house.HouseInfoDetail;
import com.hust.khanhkelvin.dto.response.house.HouseInfoInsert;
import com.hust.khanhkelvin.service.HouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class HouseResource {

    private final HouseService houseService;

    public HouseResource(HouseService houseService) {
        this.houseService = houseService;
    }

    @PostMapping("/houses")
    public ResponseEntity<HouseInfoInsert> registerHouse(@RequestBody @Valid HouseInfoRequest request) {
        HouseInfoInsert houseInfoInsert = houseService.insertHouse(request);
        return ResponseEntity.ok(houseInfoInsert);
    }

    @GetMapping("/houses")
    public ResponseEntity<List<?>> getHouses() {
        List<HouseInfoDetail> houseInfoInsert = houseService.getHouses();
        return ResponseEntity.ok(houseInfoInsert);
    }
}
