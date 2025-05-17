package com.presto.server.application.chat.message.response;

public record TypingStatusChangedEvent(
        String chatRoomId,
        String senderId,
        boolean isTyping
) {
}
