package com.presto.server.domain.chat.message.dto;

import com.presto.server.domain.chat.message.MessageCursorDirection;
import jakarta.annotation.Nullable;

public record ChatMessagesQuery(
        String chatRoomId,
        @Nullable String lastMessageId,
        @Nullable String cursorMessageId,
        MessageCursorDirection direction,
        int size
) {
}
