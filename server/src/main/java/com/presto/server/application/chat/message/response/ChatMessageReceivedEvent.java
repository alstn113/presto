package com.presto.server.application.chat.message.response;

import com.presto.server.domain.chat.message.MessageType;
import java.time.Instant;

public record ChatMessageReceivedEvent(
        String id,
        String content,
        MessageType messageType,
        Sender sender,
        Instant sentAt
) {

    public record Sender(
            String senderId,
            String username
    ) {
    }
}
