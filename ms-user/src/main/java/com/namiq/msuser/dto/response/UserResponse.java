package com.namiq.msuser.dto.response;

import com.namiq.msuser.enums.Role;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;

    private String username;

    private String email;

    private String password;

    private String fullName;

    private Role role;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
