package com.presto.server.application.chat.message.request;

import com.presto.server.infra.security.Accessor;

public record ChatMessageRequest(
        Long chatRoomId,
        String content,
        Accessor accessor
) {
}
