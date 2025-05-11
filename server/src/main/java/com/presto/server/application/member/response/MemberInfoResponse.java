package com.presto.server.application.member.response;

import java.time.Instant;

public record MemberInfoResponse(
        String id,
        String username,
        Instant createdAt
) {
}
