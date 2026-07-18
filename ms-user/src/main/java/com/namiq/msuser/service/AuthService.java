    package com.namiq.msuser.service;

    import com.namiq.msuser.dao.entity.User;
    import com.namiq.msuser.dao.repository.UserRepository;
    import com.namiq.msuser.dto.request.AuthLoginRequest;
    import com.namiq.msuser.dto.request.AuthRegisterRequest;
    import com.namiq.msuser.dto.response.AuthLoginResponse;
    import com.namiq.msuser.dto.response.AuthRegisterResponse;
    import com.namiq.msuser.enums.Role;
    import com.namiq.msuser.exception.EmailAlreadyExistsException;
    import com.namiq.msuser.exception.UserAlreadyExistsException;
    import com.namiq.msuser.mapper.AuthMapper;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;

    @Service
    @RequiredArgsConstructor
    public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final TokenStorageService tokenStorageService;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthMapper authMapper;


        public AuthRegisterResponse register( AuthRegisterRequest registerRequest) {
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                throw new EmailAlreadyExistsException(
                        "Email already exists: " + registerRequest.getEmail()
                );
            }

            if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
                throw new UserAlreadyExistsException(
                        "Username already exists: " + registerRequest.getUsername()
                );
            }

            User user = authMapper.toUser(registerRequest);
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole(Role.ROLE_USER);
            user.setIsActive(true);
          User savedUser=  userRepository.save(user);
            return authMapper.toResponse(savedUser);

        }

        public AuthLoginResponse login(AuthLoginRequest request) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(user);

            tokenStorageService.storeAccessToken(user.getUsername(), accessToken);

            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);
            return AuthLoginResponse.builder()
                    .accessToken(accessToken)
                    .tokenType("Bearer")
                    .expiresIn((jwtService.getAccessTokenExpiration()*60))
                    .build();
        }

    }