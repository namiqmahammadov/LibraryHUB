package com.namiq.msuser.controller;

import com.namiq.msuser.dto.request.AddRoleRequest;
import com.namiq.msuser.dto.request.AuthLoginRequest;
import com.namiq.msuser.dto.request.AuthRegisterRequest;
import com.namiq.msuser.dto.request.RefreshTokenRequest;
import com.namiq.msuser.dto.response.AuthLoginResponse;
import com.namiq.msuser.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AuthRegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthLoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(authService.refresh(request));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-role")
    public ResponseEntity<Void> addRole(@Valid @RequestBody AddRoleRequest request) {
        authService.addRole(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        authService.logout(authentication.getName());
        return ResponseEntity.noContent().build();
    }



}
