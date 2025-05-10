package com.presto.server.api.chat.room;

import com.presto.server.application.chat.room.ChatRoomService;
import com.presto.server.infra.security.Accessor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/api/v1/chat-rooms")
    public void getMyChatRooms(
            @AuthenticationPrincipal Accessor accessor
    ) {
    }

    @GetMapping("/api/v1/chat-rooms/available")
    public void getAvailableChatRooms(
            @AuthenticationPrincipal Accessor accessor
    ) {
    }

    @PostMapping("/api/v1/chat-rooms/{chatRoomId}/join")
    public void joinChatRoom(
            @PathVariable String chatRoomId,
            @AuthenticationPrincipal Accessor accessor
    ) {
    }

    @PostMapping("/api/v1/chat-rooms/{chatRoomId}/leave")
    public void leaveChatRoom(
            @PathVariable String chatRoomId,
            @AuthenticationPrincipal Accessor accessor
    ) {
    }
}
