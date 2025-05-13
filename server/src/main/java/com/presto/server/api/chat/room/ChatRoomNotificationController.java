package com.presto.server.api.chat.room;

import com.presto.server.application.sse.SseEmitterManager;
import com.presto.server.infra.security.Accessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class ChatRoomNotificationController {

    private final SseEmitterManager sseEmitterManager;

    @GetMapping(
            value = "/api/v1/chat-rooms/notifications",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter streamChatRoomNotifications(
            @AuthenticationPrincipal Accessor accessor
    ) {
        return sseEmitterManager.createEmitter(accessor.id());
    }
}
