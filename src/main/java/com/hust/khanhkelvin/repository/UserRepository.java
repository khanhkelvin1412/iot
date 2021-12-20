package com.hust.khanhkelvin.repository;

import com.hust.khanhkelvin.domain.UserEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    String USERS_BY_USERNAME_CACHE = "usersByUsername";

    @Query("from UserEntity u where u.username = :username")
    Optional<UserEntity> findByUsername(String username);

    @Query("from UserEntity u where u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<UserEntity> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_USERNAME_CACHE)
    Optional<UserEntity> findOneWithAuthoritiesByUsername(String login);
}
