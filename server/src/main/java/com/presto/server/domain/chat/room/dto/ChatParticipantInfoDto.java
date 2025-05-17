package com.presto.server.domain.chat.room.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ChatParticipantInfoDto(
        String id,
        String username,
        String lastReadMessageId
) {

    @QueryProjection
    public ChatParticipantInfoDto {
    }
}
