package com.presto.server.application.sse;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterManager {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final long EMITTER_TIMEOUT = TimeUnit.DAYS.toMillis(1);
    private final Map<String, SseEmitterData> emitters;

    public SseEmitterManager() {
        this.emitters = new ConcurrentHashMap<>();
    }

    public SseEmitter createEmitter(String clientId) {
        if (emitters.containsKey(clientId)) {
            removeEmitter(clientId);
        }

        SseEmitter emitter = new SseEmitter(EMITTER_TIMEOUT);
        SseEmitterData emitterData = SseEmitterData.of(emitter);

        emitter.onCompletion(() -> removeEmitter(clientId));
        emitter.onTimeout(() -> removeEmitter(clientId));
        emitter.onError(throwable -> {
            log.error("Error in SseEmitter for clientId {}: {}", clientId, throwable.getMessage(), throwable);
            removeEmitter(clientId);
        });

        emitters.put(clientId, emitterData);
        sendConnectedEvent(emitter);

        log.info("Emitter created for clientId {}. Current emitters count: {}", clientId, emitters.size());

        return emitter;
    }

    private void sendConnectedEvent(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("connected")
                    .id(Instant.now().toString()));
        } catch (Exception e) {
            log.warn("Error while sending connected event: {}", e.getMessage(), e);
        }
    }

    private void removeEmitter(String clientId) {
        try {
            if (!emitters.containsKey(clientId)) {
                return;
            }
            SseEmitterData emitterData = emitters.get(clientId);
            emitterData.emitter().complete();
        } catch (Exception e) {
            log.warn("Error while removing emitter for clientId {}: {}", clientId, e.getMessage(), e);
        } finally {
            emitters.remove(clientId);
        }
    }
}
