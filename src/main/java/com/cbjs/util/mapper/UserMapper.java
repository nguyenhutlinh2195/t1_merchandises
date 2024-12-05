package com.cbjs.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.cbjs.dto.User;
import com.cbjs.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toDto(UserEntity userEntity);
    List<User> toDtos(List<UserEntity> userEntities);
    UserEntity toEntity(User user);
    List<UserEntity> toEntities(List<User> users);
}
