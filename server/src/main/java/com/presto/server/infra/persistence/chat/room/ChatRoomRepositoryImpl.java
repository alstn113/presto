package com.presto.server.infra.persistence.chat.room;

import static com.presto.server.domain.chat.room.QChatRoom.chatRoom;
import static com.presto.server.domain.chat.room.QChatRoomParticipant.chatRoomParticipant;
import static com.presto.server.domain.member.QMember.member;

import com.presto.server.domain.chat.room.ChatRoomRepositoryCustom;
import com.presto.server.domain.chat.room.dto.ChatParticipantInfoDto;
import com.presto.server.domain.chat.room.dto.ChatRoomOverviewResponse;
import com.presto.server.domain.chat.room.dto.QChatParticipantInfoDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public ChatRoomOverviewResponse findChatRoomOverview(String chatRoomId, int size, String memberId) {
        return null;
    }

    @Override
    public List<ChatParticipantInfoDto> findChatParticipantInfos(String chatRoomId) {
        return queryFactory
                .select(new QChatParticipantInfoDto(
                        member.id,
                        member.username,
                        chatRoomParticipant.lastReadMessageId
                ))
                .from(chatRoom)
                .leftJoin(chatRoomParticipant).on(chatRoomParticipant.chatRoomId.eq(chatRoom.id))
                .leftJoin(member).on(chatRoomParticipant.memberId.eq(member.id))
                .where(chatRoom.id.eq(chatRoomId))
                .fetch();
    }
}
