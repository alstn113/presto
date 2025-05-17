package com.presto.server.application.chat.message;

import com.presto.server.application.chat.message.request.TypingStatusRequest;
import com.presto.server.application.chat.message.response.TypingStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TypingService {

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional(readOnly = true)
    public void sendMessage(TypingStatusRequest request) {
        String senderId = request.accessor().id();

        String destination = "/topic/chat/%s/typing".formatted(request.chatRoomId());
        TypingStatusChangedEvent event = new TypingStatusChangedEvent(
                request.chatRoomId(),
                senderId,
                request.isTyping()
        );
        messagingTemplate.convertAndSend(destination, event);
    }
}
