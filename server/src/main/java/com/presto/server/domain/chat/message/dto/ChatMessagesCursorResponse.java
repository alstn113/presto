package com.presto.server.domain.chat.message.dto;

import java.util.List;

public record ChatMessagesCursorResponse(
        List<ChatMessageDto> messages,
        String prevCursor,
        String nextCursor
) {
}
