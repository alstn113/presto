package com.presto.server.api.chat.room;

import com.presto.server.application.chat.room.ChatRoomQueryService;
import com.presto.server.application.chat.room.ChatRoomService;
import com.presto.server.application.chat.room.request.AvailableChatRoomPreviewsQuery;
import com.presto.server.application.chat.room.request.JoinChatRoomRequest;
import com.presto.server.application.chat.room.request.LeaveChatRoomRequest;
import com.presto.server.application.chat.room.request.JoinedChatRoomPreviewsQuery;
import com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto;
import com.presto.server.infra.security.Accessor;
import com.presto.server.support.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomQueryService chatRoomQueryService;

    @GetMapping("/api/v1/chat-rooms/joined")
    public ResponseEntity<ApiResponse<List<JoinedChatRoomPreviewDto>>> getJoinedChatRoomPreviews(
            @AuthenticationPrincipal Accessor accessor
    ) {
        JoinedChatRoomPreviewsQuery request = new JoinedChatRoomPreviewsQuery(accessor);
        List<JoinedChatRoomPreviewDto> responses = chatRoomQueryService.getJoinedChatRoomPreviews(request);

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/api/v1/chat-rooms/available")
    public ResponseEntity<ApiResponse<List<AvailableChatRoomPreviewDto>>> getAvailableChatRoomPreviews(
            @AuthenticationPrincipal Accessor accessor
    ) {
        AvailableChatRoomPreviewsQuery request = new AvailableChatRoomPreviewsQuery(accessor);
        List<AvailableChatRoomPreviewDto> responses = chatRoomQueryService.getAvailableChatRoomPreviews(request);

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PostMapping("/api/v1/chat-rooms/{chatRoomId}/join")
    public ResponseEntity<ApiResponse<?>> joinChatRoom(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal Accessor accessor
    ) {
        JoinChatRoomRequest request = new JoinChatRoomRequest(chatRoomId, accessor);
        chatRoomService.joinChatRoom(request);

        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/api/v1/chat-rooms/{chatRoomId}/leave")
    public ResponseEntity<ApiResponse<?>> leaveChatRoom(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal Accessor accessor
    ) {
        LeaveChatRoomRequest request = new LeaveChatRoomRequest(chatRoomId, accessor);
        chatRoomService.leaveChatRoom(request);

        return ResponseEntity.ok(ApiResponse.success());
    }
}
