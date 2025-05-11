package com.presto.server.application.chat.message.response;

import java.time.Instant;

public record JoinedChatRoomPreviewUpdatedEvent(
        Long chatRoomId,
        String chatRoomName,
        String lastMessage,
        Instant lastSentAt
) {
}
