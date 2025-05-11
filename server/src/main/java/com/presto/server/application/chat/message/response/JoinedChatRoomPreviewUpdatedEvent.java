package com.presto.server.application.chat.message.response;

import java.time.Instant;

public record JoinedChatRoomPreviewUpdatedEvent(
        String chatRoomId,
        String chatRoomName,
        String lastMessageContent,
        Instant lastMessageSentAt
) {
}
