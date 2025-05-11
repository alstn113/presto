package com.presto.server.application.chat.room.request;

import com.presto.server.infra.security.Accessor;

public record LeaveChatRoomRequest(
        String chatRoomId,
        Accessor accessor
) {
}
