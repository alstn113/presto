package com.presto.server.api.chat.message.request;

import com.presto.server.application.chat.message.request.TypingStatusRequest;
import com.presto.server.infra.security.Accessor;

public record TypingStatusChangeWebRequest(
        String chatRoomId,
        Boolean isTyping
) {

    public TypingStatusRequest toAppRequest(Accessor accessor) {
        return new TypingStatusRequest(chatRoomId, isTyping, accessor);
    }
}
