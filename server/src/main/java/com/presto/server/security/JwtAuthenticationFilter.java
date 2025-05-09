package com.presto.server.security;

import com.presto.server.auth.application.AuthService;
import com.presto.server.auth.application.MemberNotFoundException;
import com.presto.server.auth.application.TokenProvider;
import com.presto.server.auth.application.response.MemberInfoResponse;
import com.presto.server.auth.infra.exception.BlankTokenException;
import com.presto.server.auth.infra.exception.InvalidTokenException;
import com.presto.server.auth.infra.exception.TokenExpiredException;
import com.presto.server.common.web.TokenCookieHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final TokenCookieHandler tokenCookieHandler;
    private final AuthService authService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            Optional<String> tokenOpt = tokenCookieHandler.extractAccessToken(request);
            tokenOpt.ifPresent(this::handleToken);

            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, e);
        }
    }

    private void handleToken(String token) {
        Long memberId = extractMemberId(token);
        MemberInfoResponse memberInfo = fetchMemberInfo(memberId);

        Accessor accessor = new Accessor(memberInfo.id());
        Authentication authentication = new JwtAuthentication(accessor);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Long extractMemberId(String token) {
        try {
            return tokenProvider.getMemberId(token);
        } catch (BlankTokenException e) {
            throw new InsufficientAuthenticationException("토큰이 비어 있습니다.", e);
        } catch (TokenExpiredException e) {
            throw new CredentialsExpiredException("토큰이 만료되었습니다.", e);
        } catch (InvalidTokenException e) {
            throw new BadCredentialsException("토큰이 유효하지 않습니다.", e);
        }
    }

    private MemberInfoResponse fetchMemberInfo(Long memberId) {
        try {
            return authService.getMember(memberId);
        } catch (MemberNotFoundException e) {
            throw new BadCredentialsException("토큰에 해당하는 회원을 찾을 수 없습니다.", e);
        }
    }
}
