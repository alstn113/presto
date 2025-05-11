package com.presto.server.application.chat.room.request;

import com.presto.server.infra.security.Accessor;

public record JoinChatRoomRequest(
        String chatRoomId,
        Accessor accessor
) {
}
