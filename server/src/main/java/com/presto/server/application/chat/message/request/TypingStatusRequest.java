package com.presto.server.application.chat.message.request;

import com.presto.server.infra.security.Accessor;

public record TypingStatusRequest(
        String chatRoomId,
        Boolean isTyping,
        Accessor accessor
) {
}
