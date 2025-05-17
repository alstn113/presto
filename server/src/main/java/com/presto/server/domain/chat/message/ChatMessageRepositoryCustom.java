package com.presto.server.domain.chat.message;

import com.presto.server.domain.chat.message.dto.ChatMessagesCursorResponse;
import jakarta.annotation.Nullable;

public interface ChatMessageRepositoryCustom {

    ChatMessagesCursorResponse findChatMessagesCursor(
            String chatRoomId,
            @Nullable String cursorMessageId,
            MessageCursorDirection direction,
            int size
    );
}
