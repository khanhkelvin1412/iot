package com.hust.khanhkelvin.repository;

import com.hust.khanhkelvin.domain.SensorDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorDataEntity, Long> {
}
