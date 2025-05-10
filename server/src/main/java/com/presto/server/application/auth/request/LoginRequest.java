package com.presto.server.application.auth.request;

public record LoginRequest(
        String username,
        String password
) {
}
