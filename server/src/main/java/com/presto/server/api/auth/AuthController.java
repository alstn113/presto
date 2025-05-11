package com.presto.server.api.auth;

import com.presto.server.application.auth.AuthService;
import com.presto.server.application.auth.request.LoginRequest;
import com.presto.server.application.auth.request.RegisterRequest;
import com.presto.server.application.auth.response.TokenResponse;
import com.presto.server.infra.web.TokenCookieHandler;
import com.presto.server.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenCookieHandler tokenCookieHandler;

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        ResponseCookie accessTokenCookie = tokenCookieHandler.createAccessTokenCookie(response.token());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body(ApiResponse.success());
    }

    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request) {
        authService.register(request);

        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        ResponseCookie expiredAccessTokenCookie = tokenCookieHandler.createExpiredAccessTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredAccessTokenCookie.toString())
                .body(ApiResponse.success());

    }
}
