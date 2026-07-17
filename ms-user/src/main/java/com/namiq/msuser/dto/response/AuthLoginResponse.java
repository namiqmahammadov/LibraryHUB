package com.namiq.msuser.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthLoginResponse {
    private String accessToken;
    private String refreshToken;

}
