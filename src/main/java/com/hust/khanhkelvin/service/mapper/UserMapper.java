package com.hust.khanhkelvin.service.mapper;

import com.hust.khanhkelvin.domain.UserEntity;
import com.hust.khanhkelvin.dto.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<User, UserEntity> {
}
