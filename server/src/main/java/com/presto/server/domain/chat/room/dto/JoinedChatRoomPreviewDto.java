package com.presto.server.domain.chat.room.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.Instant;

public record JoinedChatRoomPreviewDto(
        String chatRoomId,
        String chatRoomName,
        String chatMessageId,
        String lastMessageContent,
        Instant lastMessageSentAt,
        long unreadMessageCount,
        long participantCount
) {

    @QueryProjection
    public JoinedChatRoomPreviewDto {
    }
}
