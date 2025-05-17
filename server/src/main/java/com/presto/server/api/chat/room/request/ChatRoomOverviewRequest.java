package com.presto.server.api.chat.room.request;

public record ChatRoomOverviewRequest(
        String chatRoomId,
        int size,
        String memberId
) {
}
