package com.presto.server.api.chat.message;

import com.presto.server.application.chat.message.ChatMessageQueryService;
import com.presto.server.domain.chat.message.MessageCursorDirection;
import com.presto.server.domain.chat.message.dto.ChatMessagesCursorRequest;
import com.presto.server.domain.chat.message.dto.ChatMessagesCursorResponse;
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
    public ResponseEntity<ApiResponse<ChatMessagesCursorResponse>> getChatMessagesCursor(
            @PathVariable String chatRoomId,
            @RequestParam String cursorMessageId,
            @RequestParam MessageCursorDirection direction,
            @RequestParam(defaultValue = "30") int size
    ) {
        ChatMessagesCursorRequest request = new ChatMessagesCursorRequest(chatRoomId, cursorMessageId, direction, size);
        ChatMessagesCursorResponse response = chatMessageQueryService.getChatMessagesCursor(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
