package com.presto.server.domain.chat.room.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.Instant;

public record AvailableChatRoomPreviewDto(
        String chatRoomId,
        String chatRoomName,
        Instant createdAt
) {

    @QueryProjection
    public AvailableChatRoomPreviewDto {
    }
}
