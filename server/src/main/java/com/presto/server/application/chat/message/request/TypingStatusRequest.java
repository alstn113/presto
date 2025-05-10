package com.presto.server.application.chat.message.request;

import com.presto.server.infra.security.Accessor;

public record TypingStatusRequest(
        Long chatRoomId,
        Boolean isTyping,
        Accessor accessor
) {
}
