package com.namiq.msuser.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthLoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
