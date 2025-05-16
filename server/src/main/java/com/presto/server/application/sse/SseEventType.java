package com.presto.server.application.sse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SseEventType {

    JOINED_CHAT_ROOM_PREVIEW_UPDATED("joined_chat_rooms_preview_updated"),
    ;

    private final String name;
}
