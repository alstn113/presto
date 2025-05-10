package com.presto.server.api.chat.message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatStompHandler {

    @MessageMapping("/chat.send")
    public void handleSendMessage() {

    }

    @MessageMapping("/chat.typing")
    public void handleTypingIndicator() {
    }
}
