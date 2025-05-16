package com.presto.server.application.sse;

import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Getter
public class SseEmitterData {

    private final SseEmitter emitter;
    private final Instant createdAt;
    private Instant lastEventTime;

    public static SseEmitterData of(SseEmitter emitter) {
        Instant now = Instant.now();
        return new SseEmitterData(emitter, now, now);
    }

    private SseEmitterData(SseEmitter emitter, Instant createdAt, Instant lastEventTime) {
        this.emitter = emitter;
        this.createdAt = createdAt;
        this.lastEventTime = lastEventTime;
    }

    public void updateLastEventTime() {
        this.lastEventTime = Instant.now();
    }
}
