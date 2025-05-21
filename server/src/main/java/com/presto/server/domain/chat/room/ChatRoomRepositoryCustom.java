package com.presto.server.domain.chat.room;

import com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.ChatParticipantInfoDto;
import com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepositoryCustom {

    List<ChatParticipantInfoDto> findChatParticipantInfos(String chatRoomId);

    List<JoinedChatRoomPreviewDto> findJoinedChatRoomPreviews(String memberId);

    Optional<JoinedChatRoomPreviewDto> findJoinedChatRoomPreviewByMemberId(String chatRoomId, String memberId);

    List<AvailableChatRoomPreviewDto> findAvailableChatRoomPreviews(String memberId);
}
