package com.presto.server.domain.chat.room.dto;

import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import java.util.List;

public record ChatRoomOverviewResponse(
        List<ChatMessageDto> messages,
        List<ChatParticipantInfoDto> participants,
        String selfId,
        String prevCursor,
        String nextCursor
) {
}
