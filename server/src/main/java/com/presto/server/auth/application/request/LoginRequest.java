package com.presto.server.auth.application.request;

public record LoginRequest(
        String username,
        String password
) {
}
