package com.hust.khanhkelvin.repository;

import com.hust.khanhkelvin.domain.HouseEntity;
import com.hust.khanhkelvin.domain.UserEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public interface HouseRepository extends JpaRepository<HouseEntity, Long> {
    @Query("from HouseEntity h where h.name = :name and h.user.id = :id")
    Optional<HouseEntity> existHouse(@Param("name") String name, @Param("id") Long userId);

    @Query("from HouseEntity h where h.user.id = :id")
    Optional<Set<HouseEntity>> findByUserId(@Param("id") Long userId);
}
