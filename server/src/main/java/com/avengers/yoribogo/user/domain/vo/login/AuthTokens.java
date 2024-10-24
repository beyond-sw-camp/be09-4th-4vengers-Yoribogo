package com.avengers.yoribogo.user.domain.vo.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokens {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long accessTokenExpiry;
    private long refreshTokenExpiry;
    private String userAuthId;
}

