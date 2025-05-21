package com.presto.server.infra.persistence.chat.room;

import static com.presto.server.domain.chat.room.QChatRoom.chatRoom;
import static com.presto.server.domain.chat.room.QChatRoomParticipant.chatRoomParticipant;
import static com.presto.server.domain.member.QMember.member;

import com.presto.server.domain.chat.message.QChatMessage;
import com.presto.server.domain.chat.room.ChatRoomRepositoryCustom;
import com.presto.server.domain.chat.room.QChatRoom;
import com.presto.server.domain.chat.room.QChatRoomParticipant;
import com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.ChatParticipantInfoDto;
import com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.QAvailableChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.QChatParticipantInfoDto;
import com.presto.server.domain.chat.room.dto.QJoinedChatRoomPreviewDto;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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

    @Override
    public List<JoinedChatRoomPreviewDto> findJoinedChatRoomPreviews(String memberId) {
        QChatRoom cr = QChatRoom.chatRoom;
        QChatRoomParticipant userPart = new QChatRoomParticipant("userPart");
        QChatRoomParticipant roomPart = new QChatRoomParticipant("roomPart");
        QChatMessage cmLatest = new QChatMessage("cmLatest");
        QChatMessage cm2 = new QChatMessage("cm2");
        QChatMessage unreadMsg = new QChatMessage("unreadMsg");

        return queryFactory
                .select(new QJoinedChatRoomPreviewDto(
                        cr.id,
                        cr.name,
                        cmLatest.id,
                        cmLatest.content,
                        cmLatest.createdAt,
                        unreadMsg.countDistinct(),
                        roomPart.countDistinct()
                ))
                .from(userPart)
                .join(cr).on(userPart.chatRoomId.eq(cr.id)
                        .and(userPart.memberId.eq(memberId)))
                .leftJoin(cmLatest).on(cmLatest.chatRoomId.eq(cr.id)
                        .and(cmLatest.createdAt.eq(
                                JPAExpressions.select(cm2.createdAt.max())
                                        .from(cm2)
                                        .where(cm2.chatRoomId.eq(cr.id))
                        ))
                )
                .leftJoin(unreadMsg).on(unreadMsg.chatRoomId.eq(cr.id)
                        .and(userPart.lastReadMessageId.isNull()
                                .or(unreadMsg.id.gt(userPart.lastReadMessageId)))
                )
                .leftJoin(roomPart).on(roomPart.chatRoomId.eq(cr.id))
                .groupBy(cr.id, cr.name, cmLatest.content, cmLatest.createdAt)
                .orderBy(cmLatest.createdAt.desc().nullsLast())
                .fetch();
    }

    @Override
    public Optional<JoinedChatRoomPreviewDto> findJoinedChatRoomPreviewByMemberId(String chatRoomId, String memberId) {
        QChatMessage cmLatest = new QChatMessage("cmLatest");
        QChatMessage cmSub = new QChatMessage("cmSub");
        QChatMessage unreadMsg = new QChatMessage("chatMessage");
        QChatRoomParticipant roomPart = new QChatRoomParticipant("roomPart");
        QChatRoomParticipant userPart = new QChatRoomParticipant("userPart");

        JoinedChatRoomPreviewDto result = queryFactory
                .select(new QJoinedChatRoomPreviewDto(
                        chatRoom.id,
                        chatRoom.name,
                        cmLatest.id,
                        cmLatest.content,
                        cmLatest.createdAt,
                        unreadMsg.countDistinct(),
                        roomPart.countDistinct()
                ))
                .from(userPart)
                .join(chatRoom).on(userPart.chatRoomId.eq(chatRoom.id)
                        .and(userPart.memberId.eq(memberId)))
                .leftJoin(cmLatest).on(cmLatest.chatRoomId.eq(chatRoom.id)
                        .and(cmLatest.createdAt.eq(
                                queryFactory
                                        .select(cmSub.createdAt.max())
                                        .from(cmSub)
                                        .where(cmSub.chatRoomId.eq(chatRoom.id))
                        )))
                .leftJoin(unreadMsg).on(unreadMsg.chatRoomId.eq(chatRoom.id)
                        .and(userPart.lastReadMessageId.isNull()
                                .or(unreadMsg.id.gt(userPart.lastReadMessageId))))
                .leftJoin(roomPart).on(roomPart.chatRoomId.eq(chatRoom.id))
                .where(chatRoom.id.eq(chatRoomId))
                .groupBy(chatRoom.id, chatRoom.name, cmLatest.id, cmLatest.content, cmLatest.createdAt)
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<AvailableChatRoomPreviewDto> findAvailableChatRoomPreviews(String memberId) {
        return queryFactory
                .select(new QAvailableChatRoomPreviewDto(
                        chatRoom.id,
                        chatRoom.name,
                        chatRoom.createdAt
                ))
                .from(chatRoom)
                .leftJoin(chatRoomParticipant)
                .on(chatRoomParticipant.chatRoomId.eq(chatRoom.id)
                        .and(chatRoomParticipant.memberId.eq(memberId)))
                .where(chatRoomParticipant.memberId.isNull())
                .fetch();
    }
}
