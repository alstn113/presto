package com.presto.server.application.chat.message;

import com.presto.server.domain.CursorResult;
import com.presto.server.domain.chat.message.ChatMessageRepository;
import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import com.presto.server.domain.chat.message.dto.ChatMessagesQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional(readOnly = true)
    public CursorResult<ChatMessageDto> getChatMessages(ChatMessagesQuery query) {
        return chatMessageRepository.findChatMessages(query);
    }
}
