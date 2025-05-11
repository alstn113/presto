package com.presto.server.domain.chat.room.dto;

import java.time.Instant;

public record AvailableChatRoomPreviewDto(
        Long chatRoomId,
        String chatRoomName,
        Instant createdAt
) {
}
