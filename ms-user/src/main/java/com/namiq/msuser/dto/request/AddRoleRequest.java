package com.namiq.msuser.dto.request;

import com.namiq.msuser.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddRoleRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotNull(message = "Role cannot be null")
    private Role role;
}
