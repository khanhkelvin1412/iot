package com.hust.khanhkelvin.repository;

import com.hust.khanhkelvin.domain.HouseEntity;
import com.hust.khanhkelvin.domain.HouseSensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public interface HouseSensorRepository extends JpaRepository<HouseSensorEntity, Long> {

    @Query("from HouseSensorEntity h where h.houseId = :id")
    Optional<Set<HouseSensorEntity>> findHouseSensorByHouseID(@Param("id") Long houseId);

    @Query("from HouseSensorEntity h where h.houseId = :houseId")
    List<HouseSensorEntity> findByHouseId(Long houseId);
}
