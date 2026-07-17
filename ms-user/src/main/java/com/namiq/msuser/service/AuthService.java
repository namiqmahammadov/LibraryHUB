package com.namiq.msuser.service;

import com.namiq.msuser.dao.entity.User;
import com.namiq.msuser.dao.repository.UserRepository;
import com.namiq.msuser.dto.request.AddRoleRequest;
import com.namiq.msuser.dto.request.AuthLoginRequest;
import com.namiq.msuser.dto.request.AuthRegisterRequest;
import com.namiq.msuser.dto.request.RefreshTokenRequest;
import com.namiq.msuser.dto.response.AuthLoginResponse;
import com.namiq.msuser.enums.Role;
import com.namiq.msuser.exception.EmailAlreadyExistsException;
import com.namiq.msuser.exception.InvalidRefreshTokenException;
import com.namiq.msuser.exception.UserAlreadyExistsException;
import com.namiq.msuser.exception.UserNotFoundException;
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
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;


    public void register(@Valid AuthRegisterRequest registerRequest) {
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
        userRepository.save(user);


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
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenStorageService.storeAccessToken(user.getUsername(), accessToken);
        tokenStorageService.storeRefreshToken(user.getUsername(), refreshToken);
        user.setLastLoginTime(LocalDateTime.now());
           userRepository.save(user);
        return AuthLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthLoginResponse refresh(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)
                || !jwtService.isRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);

        if (!tokenStorageService.isRefreshTokenValid(username, refreshToken)) {
            throw new InvalidRefreshTokenException("Refresh token is not active");
        }

        User user = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        tokenStorageService.storeAccessToken(username, newAccessToken);
        tokenStorageService.storeRefreshToken(username, newRefreshToken);

        return AuthLoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void addRole(AddRoleRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found: " + request.getUsername()));

        user.setRole(request.getRole());

        userRepository.save(user);
    }

    public void logout(String username) {
        tokenStorageService.removeAccessToken(username);
        tokenStorageService.removeRefreshToken(username);
    }
}