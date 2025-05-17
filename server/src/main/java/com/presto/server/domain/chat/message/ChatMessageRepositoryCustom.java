package com.presto.server.domain.chat.message;

import com.presto.server.domain.CursorResult;
import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import com.presto.server.domain.chat.message.dto.ChatMessagesRequest;

public interface ChatMessageRepositoryCustom {

    CursorResult<ChatMessageDto> findChatMessages(ChatMessagesRequest request);
}
