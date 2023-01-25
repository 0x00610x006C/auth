package com.scltools.auth.service;

import lombok.Getter;

public class Login
{
    @Getter
    private final Token accessToken;

    @Getter
    private final Token refreshToken;

    private final static Long ACCESS_TOKEN_VALIDITY = 1L;

    private final static Long REFRESH_TOKEN_VALIDITY = 1440L;

    private Login(Token accessToken, Token refreshToken)
    {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static Login of(Long userId, String accessSecret, String refreshSecret)
    {
        return new Login(
                Token.of(userId, ACCESS_TOKEN_VALIDITY, accessSecret),
                Token.of(userId, REFRESH_TOKEN_VALIDITY, refreshSecret)
        );
    }

    public static Login of(Long userId, String accessSecret, Token refreshToken)
    {
        return new Login(
                Token.of(userId, ACCESS_TOKEN_VALIDITY, accessSecret),
                refreshToken
        );
    }
}
