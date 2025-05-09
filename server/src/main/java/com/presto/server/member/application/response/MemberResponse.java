package com.presto.server.member.application.response;

import java.time.Instant;

public record MemberResponse(
        Long id,
        String username,
        Instant createdAt
) {
}
