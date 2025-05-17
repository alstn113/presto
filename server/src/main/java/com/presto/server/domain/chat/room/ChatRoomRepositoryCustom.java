package com.presto.server.domain.chat.room;

import com.presto.server.domain.chat.room.dto.ChatParticipantInfoDto;
import com.presto.server.domain.chat.room.dto.ChatRoomOverviewResponse;
import java.util.List;

public interface ChatRoomRepositoryCustom {

    ChatRoomOverviewResponse findChatRoomOverview(String chatRoomId, int size, String memberId);

    List<ChatParticipantInfoDto> findChatParticipantInfos(String chatRoomId);
}
