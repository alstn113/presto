package com.presto.server.domain.chat.message.dto;

import com.presto.server.domain.chat.message.MessageCursorDirection;

public record ChatMessagesCursorRequest(
        String chatRoomId,
        String cursorMessageId,
        MessageCursorDirection direction,
        int size
) {
}
