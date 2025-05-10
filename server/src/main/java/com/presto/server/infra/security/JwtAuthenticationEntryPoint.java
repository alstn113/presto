package com.presto.server.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.presto.server.infra.web.TokenCookieHandler;
import com.presto.server.support.error.ErrorType;
import com.presto.server.support.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final TokenCookieHandler tokenCookieHandler;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        ResponseCookie expiredAccessTokenCookie = tokenCookieHandler.createExpiredAccessTokenCookie();

        response.setHeader(HttpHeaders.SET_COOKIE, expiredAccessTokenCookie.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ApiResponse<?> errorResponse = ApiResponse.error(ErrorType.UNAUTHORIZED);
        String body = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(body);
    }
}
