package com.presto.server.domain.chat.room.dto;

import java.time.Instant;

public record JoinedChatRoomPreviewDto(
        String chatRoomId,
        String chatRoomName,
        String lastMessageContent,
        Instant lastMessageSentAt,
        long unreadMessageCount,
        long participantCount
) {
}
