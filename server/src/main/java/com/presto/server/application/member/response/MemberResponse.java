package com.presto.server.application.member.response;

import java.time.Instant;

public record MemberResponse(
        Long id,
        String username,
        Instant createdAt
) {
}
