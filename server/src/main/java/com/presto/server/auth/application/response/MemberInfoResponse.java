package com.presto.server.auth.application.response;

import java.time.Instant;

public record MemberInfoResponse(
        Long id,
        String username,
        Instant createdAt
) {
}
