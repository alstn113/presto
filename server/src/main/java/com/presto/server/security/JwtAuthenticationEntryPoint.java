package com.presto.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.presto.server.common.web.TokenCookieHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                createErrorMessage(exception)
        );
        String body = objectMapper.writeValueAsString(detail);

        response.getWriter().write(body);
    }

    /**
     * Spring Security의 ExceptionTranslationFilter에서 "인증이 필요하지만 인증되지 않았을 경우" handleAccessDeniedException 메서드 중
     * this.sendStartAuthentication 메서드를 사용한다. 여기서 new InsufficientAuthenticationException("...")이라고 고정된 메세지를 사용하기 때문에
     * 커스텀할 수 없다.
     */
    private String createErrorMessage(AuthenticationException exception) {
        if (exception == null || exception instanceof InsufficientAuthenticationException) {
            return "인증이 필요합니다.";
        }

        return exception.getMessage();
    }
}
