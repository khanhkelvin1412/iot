package com.hust.khanhkelvin.service.mapper;

import com.hust.khanhkelvin.domain.UserEntity;
import com.hust.khanhkelvin.dto.response.UserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInfoMapper extends EntityMapper<UserInfo, UserEntity>{
    UserInfo toDto(UserEntity userEntity);
}
