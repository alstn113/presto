package com.presto.server.application.sse;

import java.time.Instant;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public record SseEmitterData(
        SseEmitter emitter,
        Instant createdAt,
        Instant lastEventTime
) {

    public static SseEmitterData of(SseEmitter emitter) {
        Instant now = Instant.now();
        return new SseEmitterData(emitter, now, now);
    }

    public SseEmitterData updateLastEventTime() {
        return new SseEmitterData(emitter, createdAt, Instant.now());
    }
}
