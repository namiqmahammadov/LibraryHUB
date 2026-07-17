package com.namiq.msuser.dto.response;

import com.namiq.msuser.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AuthRegisterResponse {
    private Integer id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
