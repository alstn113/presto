package com.presto.server.application.chat.message.response;

public record TypingStatusEvent(
        Long chatRoomId,
        Sender sender,
        boolean isTyping
) {

    public record Sender(
            Long id,
            String username
    ) {
    }
}
