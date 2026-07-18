package com.namiq.msuser.controller;

import com.namiq.msuser.dto.request.AuthLoginRequest;
import com.namiq.msuser.dto.request.AuthRegisterRequest;
import com.namiq.msuser.dto.response.AuthLoginResponse;
import com.namiq.msuser.dto.response.AuthRegisterResponse;
import com.namiq.msuser.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AuthRegisterResponse> register(
            @Valid @RequestBody AuthRegisterRequest request) {

        System.out.println("Controller started");

        AuthRegisterResponse response = authService.register(request);

        System.out.println("Controller finished");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}
