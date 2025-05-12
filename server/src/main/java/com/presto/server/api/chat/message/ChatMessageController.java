package com.presto.server.api.chat.message;

import com.presto.server.application.chat.message.ChatMessageQueryService;
import com.presto.server.domain.CursorPaginatedResponse;
import com.presto.server.domain.chat.message.MessageCursorDirection;
import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import com.presto.server.domain.chat.message.dto.ChatMessagesQuery;
import com.presto.server.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageQueryService chatMessageQueryService;

    @GetMapping("/api/v1/chat-rooms/{chatRoomId}/messages")
    public ResponseEntity<ApiResponse<CursorPaginatedResponse<ChatMessageDto>>> getChatMessages(
            @PathVariable String chatRoomId,
            @RequestParam(required = false) String messageId,
            @RequestParam(defaultValue = "NEXT") MessageCursorDirection direction,
            @RequestParam(defaultValue = "50") int size
    ) {
        ChatMessagesQuery query = new ChatMessagesQuery(chatRoomId, messageId, direction, size);
        CursorPaginatedResponse<ChatMessageDto> response = chatMessageQueryService.getChatMessages(query);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
