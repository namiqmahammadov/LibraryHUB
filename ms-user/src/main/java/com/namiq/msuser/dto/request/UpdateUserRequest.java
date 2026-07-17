package com.namiq.msuser.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Full name cannot be empty")
    private String fullName;
}
