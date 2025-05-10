package com.presto.server.application.auth.request;

public record RegisterRequest(
        String username,
        String password
) {
}
