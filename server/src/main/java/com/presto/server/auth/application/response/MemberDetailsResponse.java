package com.presto.server.auth.application.response;

import java.time.Instant;

public record MemberDetailsResponse(
        Long id,
        String username,
        Instant createdAt
) {
}
