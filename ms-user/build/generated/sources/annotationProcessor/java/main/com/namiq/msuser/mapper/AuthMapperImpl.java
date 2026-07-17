package com.namiq.msuser.mapper;

import com.namiq.msuser.dao.entity.User;
import com.namiq.msuser.dto.request.AuthRegisterRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-16T23:02:19+0400",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-java-compiler-worker-9.5.1.jar, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class AuthMapperImpl implements AuthMapper {

    @Override
    public User toUser(AuthRegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( request.getUsername() );
        user.email( request.getEmail() );
        user.fullName( request.getFullName() );

        return user.build();
    }
}
