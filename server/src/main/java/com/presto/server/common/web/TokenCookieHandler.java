package com.presto.server.common.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenCookieHandler {

    private final TokenCookieProperties properties;

    public ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from(properties.key(), token)
                .httpOnly(properties.httpOnly())
                .secure(properties.secure())
                .domain(properties.domain())
                .path(properties.path())
                .maxAge(properties.maxAge())
                .build();
    }

    public ResponseCookie createExpiredAccessTokenCookie() {
        return ResponseCookie.from(properties.key(), "")
                .httpOnly(properties.httpOnly())
                .secure(properties.secure())
                .domain(properties.domain())
                .path(properties.path())
                .maxAge(0)
                .build();
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String tokenCookieName = properties.key();
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> tokenCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
