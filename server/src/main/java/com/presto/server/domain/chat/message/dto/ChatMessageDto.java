package com.presto.server.domain.chat.message.dto;

import com.presto.server.domain.chat.message.MessageType;
import com.querydsl.core.annotations.QueryProjection;
import java.time.Instant;

public record ChatMessageDto(
        String id,
        String content,
        MessageType messageType,
        Sender sender,
        Instant sentAt
) {

    @QueryProjection
    public ChatMessageDto {
    }

    public record Sender(
            String id,
            String username
    ) {

        @QueryProjection
        public Sender {
        }
    }
}
