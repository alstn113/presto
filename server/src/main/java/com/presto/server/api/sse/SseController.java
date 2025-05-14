package com.presto.server.api.sse;

import com.presto.server.application.sse.SseEmitterManager;
import com.presto.server.infra.security.Accessor;
import java.io.IOException;
import java.time.Instant;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterManager sseEmitterManager;

    @GetMapping(
            value = "/api/v1/sse",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter streamChatRoomNotifications(
            @AuthenticationPrincipal Accessor accessor
    ) {
        return sseEmitterManager.createEmitter(accessor.id());
    }

    @GetMapping("/api/v1/sse/active")
    public void streamActiveChatRoomNotifications(
            @AuthenticationPrincipal Accessor accessor
    ) {
        while (true) {
            try {
                Thread.sleep(1000);
                SseEmitter emitter = sseEmitterManager.getEmitter(accessor.id());
                if (emitter != null) {
                    Random random = new Random();
                    int randomNumber = random.nextInt(100);
                    emitter.send(SseEmitter.event()
                            .name("test-event")
                            .data("Random number: 1")
                            .id(Instant.now().toString()));

                    // Simulate some processing time
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
