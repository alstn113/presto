package com.presto.server.auth.application.request;

public record RegisterRequest(
        String username,
        String password
) {
}
