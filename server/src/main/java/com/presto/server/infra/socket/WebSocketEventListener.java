package com.presto.server.infra.socket;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("🟢 WebSocket 연결됨 - sessionId: {}", accessor.getSessionId());
    }

    @EventListener
    public void handleSessionDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("🔴 WebSocket 연결 끊김 - sessionId: {}", accessor.getSessionId());
    }
}
