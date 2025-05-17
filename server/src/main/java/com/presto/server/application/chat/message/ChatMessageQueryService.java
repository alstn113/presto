package com.presto.server.application.chat.message;

import com.presto.server.domain.chat.message.ChatMessageRepository;
import com.presto.server.domain.chat.message.dto.ChatMessagesCursorRequest;
import com.presto.server.domain.chat.message.dto.ChatMessagesCursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional(readOnly = true)
    public ChatMessagesCursorResponse getChatMessagesCursor(ChatMessagesCursorRequest request) {
        return chatMessageRepository.findChatMessagesCursor(
                request.chatRoomId(),
                request.cursorMessageId(),
                request.direction(),
                request.size()
        );
    }
}
