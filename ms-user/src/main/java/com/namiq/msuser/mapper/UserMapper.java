package com.namiq.msuser.mapper;

import com.namiq.msuser.dao.entity.User;
import com.namiq.msuser.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

}