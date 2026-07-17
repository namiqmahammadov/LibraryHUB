    package com.namiq.msuser.service;

    import com.namiq.msuser.dao.entity.User;
    import com.namiq.msuser.dao.repository.UserRepository;
    import com.namiq.msuser.dto.request.UpdateUserRequest;
    import com.namiq.msuser.dto.response.UserResponse;
    import com.namiq.msuser.exception.UserNotFoundException;
    import com.namiq.msuser.mapper.UserMapper;
    import jakarta.transaction.Transactional;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Service;

    import java.util.Collections;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class UserService {
        private final UserRepository userRepository;
        private final UserMapper userMapper;

        public UserResponse getCurrentUser() {
          User user=  userRepository.findByUsername(getUser()).orElseThrow(()->
               new UserNotFoundException("User not found")
            );
          return userMapper.toResponse(user);
        }
        private String getUser() {

           return SecurityContextHolder.getContext().getAuthentication().getName();

        }

        public UserResponse updateUser( UpdateUserRequest updateUserRequest) {
            User user=  userRepository.findByUsername(getUser()).orElseThrow(()->
                    new UserNotFoundException("User not found")
            );
            user.setFullName(updateUserRequest.getFullName());
            user.setEmail(updateUserRequest.getEmail());
            userRepository.save(user);
           return userMapper.toResponse(user);

        }

        public UserResponse getUserById(Integer id) {
          User user=userRepository.findById(id).orElseThrow(()->
                  new UserNotFoundException("User not found"));
            return userMapper.toResponse(user);
        }

        public List<UserResponse> getAllUsers() {
            return userRepository.findAll().stream()
                   .map(userMapper::toResponse)
                   .toList();

        }
        @Transactional
        public void deleteUserById(Integer id) {
            User user=userRepository.findById(id).orElseThrow(()->
                    new UserNotFoundException("User not found"));
            if(!user.getIsActive()){
                throw new UserNotFoundException("User is already deleted");
            }
            user.setIsActive(false);
            userRepository.save(user);

        }
    }
