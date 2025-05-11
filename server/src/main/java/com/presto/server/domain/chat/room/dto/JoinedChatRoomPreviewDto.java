package com.presto.server.domain.chat.room.dto;

import java.time.Instant;

// TODO unreadMessageCount, participantCount 추가 예정
public record JoinedChatRoomPreviewDto(
        Long chatRoomId,
        String chatRoomName,
        String lastMessageContent,
        Instant lastMessageSentAt
) {
}
