package com.presto.server.domain.chat.message;

import com.presto.server.domain.CursorPaginatedResponse;
import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import com.presto.server.domain.chat.message.dto.ChatMessagesQuery;

public interface ChatMessageRepositoryCustom {

    CursorPaginatedResponse<ChatMessageDto> findChatMessages(ChatMessagesQuery query);
}
