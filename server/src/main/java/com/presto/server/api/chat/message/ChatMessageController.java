package com.presto.server.api.chat.message;

import com.presto.server.application.chat.message.ChatMessageQueryService;
import com.presto.server.domain.CursorResult;
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

    // INIT, lastMessageId == null -> 이전 메세지들
    // INIT, lastMessageId != null -> 이전, 이후 메세지들
    // PREV, cursorMessageId
    // NEXT, cursorMessageId
    @GetMapping("/api/v1/chat-rooms/{chatRoomId}/messages")
    public ResponseEntity<ApiResponse<CursorResult<ChatMessageDto>>> getChatMessages(
            @PathVariable String chatRoomId,
            @RequestParam(required = false) String lastMessageId,
            @RequestParam(required = false) String cursorMessageId,
            @RequestParam(defaultValue = "INIT") MessageCursorDirection direction,
            @RequestParam(defaultValue = "30") int size
    ) {
        ChatMessagesQuery query = new ChatMessagesQuery(chatRoomId, lastMessageId, cursorMessageId, direction, size);
        CursorResult<ChatMessageDto> response = chatMessageQueryService.getChatMessages(query);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
