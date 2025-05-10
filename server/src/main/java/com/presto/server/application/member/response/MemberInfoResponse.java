package com.presto.server.application.member.response;

import java.time.Instant;

public record MemberInfoResponse(
        Long id,
        String username,
        Instant createdAt
) {
}
