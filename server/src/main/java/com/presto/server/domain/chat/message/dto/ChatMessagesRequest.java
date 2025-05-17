package com.presto.server.domain.chat.message.dto;

import com.presto.server.domain.chat.message.MessageCursorDirection;
import jakarta.annotation.Nullable;

public record ChatMessagesRequest(
        String chatRoomId,
        @Nullable String cursorMessageId,
        MessageCursorDirection direction,
        int size,
        String memberId
) {
}
