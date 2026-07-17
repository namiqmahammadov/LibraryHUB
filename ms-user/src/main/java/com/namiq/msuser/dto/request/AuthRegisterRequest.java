package com.namiq.msuser.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterRequest {

    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 3, max = 50,
            message = "Username must be between 3 and 50 characters.")
    private String username;

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8,
            message = "Password must be at least 8 characters long.")
    private String password;

    @Size(max = 100,
            message = "Full name cannot exceed 100 characters.")
    private String fullName;
}