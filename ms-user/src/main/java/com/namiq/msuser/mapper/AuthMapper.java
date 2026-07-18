package com.namiq.msuser.mapper;

import com.namiq.msuser.dao.entity.User;
import com.namiq.msuser.dto.request.AuthRegisterRequest;
import com.namiq.msuser.dto.response.AuthRegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    User toUser(AuthRegisterRequest request);

    AuthRegisterResponse toResponse(User user);
}