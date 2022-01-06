package com.hust.khanhkelvin.repository;

import com.hust.khanhkelvin.domain.AuthorityEntity;
import com.hust.khanhkelvin.dto.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Authority> {
    Optional<AuthorityEntity> findByName(String name);
}
