package com.presto.server.application.chat.message.response;

import com.presto.server.domain.chat.message.MessageType;
import java.time.Instant;

public record ChatMessageReceivedEvent(
        String id,
        String content,
        MessageType messageType,
        String senderId,
        Instant sentAt
) {
}
