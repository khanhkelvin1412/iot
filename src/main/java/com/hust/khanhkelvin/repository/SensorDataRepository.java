package com.hust.khanhkelvin.repository;

import com.hust.khanhkelvin.domain.SensorDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorDataEntity, Long> {

    @Query("from SensorDataEntity sde where sde.houseSensorId in (:houseSensorIds)")
    List<SensorDataEntity> findAllByHouseSensorIds(List<Long> houseSensorIds);
}
