package com.presto.server.application.sse;

import com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterService {

    private static final long EMITTER_TIMEOUT = TimeUnit.DAYS.toMillis(1);

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<String, SseEmitterData> emitters = new ConcurrentHashMap<>();

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

    public void sendChatRoomPreviewUpdatedEvent(
            String memberId,
            JoinedChatRoomPreviewDto event
    ) {
        sendEvent(
                memberId,
                SseEventType.JOINED_CHAT_ROOM_PREVIEW_UPDATED.getName(),
                event
        );
    }

    private void sendConnectedEvent(SseEmitter emitter) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .name("connected")
                            .data("connected")
                            .id(Instant.now().toString())
            );
        } catch (Exception e) {
            log.warn("Error while sending connected event: {}", e.getMessage(), e);
        }
    }

    private void sendEvent(
            String memberId,
            String eventType,
            Object data
    ) {
        if (!emitters.containsKey(memberId)) {
            return;
        }

        SseEmitterData emitterData = emitters.get(memberId);

        try {
            emitterData.getEmitter().send(
                    SseEmitter.event()
                            .name(eventType)
                            .data(data)
                            .id(Instant.now().toString())
            );

            emitterData.updateLastEventTime();
        } catch (Exception e) {
            log.warn("Error while sending event to clientId {}: {}", memberId, e.getMessage(), e);
            removeEmitter(memberId);
        }
    }

    private void removeEmitter(String clientId) {
        try {
            if (!emitters.containsKey(clientId)) {
                return;
            }
            SseEmitterData emitterData = emitters.get(clientId);
            emitterData.getEmitter().complete();
        } catch (Exception e) {
            log.warn("Error while removing emitter for clientId {}: {}", clientId, e.getMessage(), e);
        } finally {
            emitters.remove(clientId);
        }
    }
}
