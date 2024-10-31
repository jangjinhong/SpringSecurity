package com.eatda.login.form;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class JWToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiresIn;
    private Date refreshTokenExpiresIn;
}
