package com.presto.server.security;

import com.presto.server.auth.application.TokenProvider;
import com.presto.server.common.web.TokenCookieHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final TokenCookieHandler tokenCookieHandler;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            Optional<String> tokenOpt = tokenCookieHandler.extractAccessToken(request);
            tokenOpt.ifPresent(token -> handleToken(token, request));

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, null);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleToken(String token, HttpServletRequest request) {
        // 1. 토큰에서 유저 정보를 가져온다.
        // 2. 데이터베이스에서 유저 정보를 가져온다.
        //
        if (tokenProvider.isValid(token)) {
            Authentication authentication = authenticationService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
