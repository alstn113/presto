package com.presto.server.api.sse;

import com.presto.server.application.sse.SseEmitterService;
import com.presto.server.infra.security.Accessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(
            value = "/api/v1/sse",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter handleConnect(
            @AuthenticationPrincipal Accessor accessor
    ) {
        return sseEmitterService.createEmitter(accessor.id());
    }
}
