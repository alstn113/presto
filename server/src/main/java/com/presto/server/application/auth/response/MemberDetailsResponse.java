package com.presto.server.application.auth.response;

import java.time.Instant;

public record MemberDetailsResponse(
        Long id,
        String username,
        Instant createdAt
) {
}
