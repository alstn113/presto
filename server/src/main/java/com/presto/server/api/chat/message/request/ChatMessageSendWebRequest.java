package com.presto.server.api.chat.message.request;

import com.presto.server.application.chat.message.request.ChatMessageRequest;
import com.presto.server.infra.security.Accessor;

public record ChatMessageSendWebRequest(
        String chatRoomId,
        String content
) {

    public ChatMessageRequest toAppRequest(Accessor accessor) {
        return new ChatMessageRequest(chatRoomId, content, accessor);
    }
}
