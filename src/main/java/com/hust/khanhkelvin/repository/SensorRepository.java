package com.hust.khanhkelvin.repository;

import com.hust.khanhkelvin.domain.HouseSensorEntity;
import com.hust.khanhkelvin.domain.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public interface SensorRepository extends JpaRepository<SensorEntity, Long> {

    @Query("from SensorEntity s where s.id in :ids")
    Optional<Set<SensorEntity>> findSensorByIds(@Param("ids")List<Long> ids);
}
