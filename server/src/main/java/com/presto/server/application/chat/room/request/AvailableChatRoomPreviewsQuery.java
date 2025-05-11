package com.presto.server.application.chat.room.request;

import com.presto.server.infra.security.Accessor;

public record AvailableChatRoomPreviewsQuery(
        Accessor accessor
) {
}
