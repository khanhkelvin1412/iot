package com.hust.khanhkelvin.dto.response.sensor;

import com.hust.khanhkelvin.utils.SensorType;
import lombok.Data;

@Data
public class SensorDetail {
    private Long idx;

    private Long id;

    private String name;

    private SensorType type;

    private boolean isMulti;
}
