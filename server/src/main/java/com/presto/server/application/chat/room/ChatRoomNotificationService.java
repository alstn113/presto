package com.presto.server.application.chat.room;

import com.presto.server.application.sse.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomNotificationService {

    private final SseEmitterManager sseEmitterManager;


}
