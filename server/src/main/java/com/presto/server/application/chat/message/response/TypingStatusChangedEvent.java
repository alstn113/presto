package com.presto.server.application.chat.message.response;

public record TypingStatusChangedEvent(
        String chatRoomId,
        Sender sender,
        boolean isTyping
) {

    public record Sender(
            String id,
            String username
    ) {
    }
}
