package com.presto.server.api.chat.message;

import com.presto.server.api.chat.message.request.ChatMessageSendWebRequest;
import com.presto.server.api.chat.message.request.TypingStatusChangeWebRequest;
import com.presto.server.application.chat.message.ChatMessageService;
import com.presto.server.application.chat.message.TypingService;
import com.presto.server.application.chat.message.request.ChatMessageRequest;
import com.presto.server.application.chat.message.request.TypingStatusRequest;
import com.presto.server.infra.security.Accessor;
import com.presto.server.infra.security.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageStompHandler {

    private final ChatMessageService chatMessageService;
    private final TypingService typingService;

    @MessageMapping("/chat.send") // 서버가 받는 주소, 클라이언트는 /app/chat.send 
    public void handleSendMessage(
            ChatMessageSendWebRequest webRequest,
            JwtAuthentication authentication
    ) {
        Accessor accessor = authentication.getPrincipal();
        ChatMessageRequest request = webRequest.toAppRequest(accessor);
        chatMessageService.sendMessage(request);
    }

    @MessageMapping("/chat.typing")
    public void handleTypingStatus(
            TypingStatusChangeWebRequest webRequest,
            JwtAuthentication authentication
    ) {
        Accessor accessor = authentication.getPrincipal();
        TypingStatusRequest request = webRequest.toAppRequest(accessor);
        typingService.sendMessage(request);
    }
}
