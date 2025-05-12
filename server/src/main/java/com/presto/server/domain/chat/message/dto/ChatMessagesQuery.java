package com.presto.server.domain.chat.message.dto;

import com.presto.server.domain.chat.message.MessageCursorDirection;

public record ChatMessagesQuery(
        String chatRoomId,
        String messageId,
        MessageCursorDirection direction,
        int size
) {
}
