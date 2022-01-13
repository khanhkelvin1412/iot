package com.hust.khanhkelvin.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthToken {
    public final static String TOKEN_TYPE_BEARER = "Bearer";

    private String accessToken;

    private long expiresIn;

    private String username;

    private String tokenType;
}
