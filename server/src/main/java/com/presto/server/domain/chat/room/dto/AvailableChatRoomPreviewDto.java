package com.presto.server.domain.chat.room.dto;

import java.time.Instant;

public record AvailableChatRoomPreviewDto(
        String chatRoomId,
        String chatRoomName,
        Instant createdAt
) {
}
