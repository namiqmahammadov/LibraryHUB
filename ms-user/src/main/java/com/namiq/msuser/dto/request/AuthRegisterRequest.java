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
    @NotBlank(message = "username is not null")
    @Size(min = 3,max = 50,message = "username must be between 3 and 50 size ")
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min =8)
    private String password;
    @Size(max = 100)
    private String fullName;
}
