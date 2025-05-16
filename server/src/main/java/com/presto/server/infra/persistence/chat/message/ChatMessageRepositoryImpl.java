package com.presto.server.infra.persistence.chat.message;

import static com.presto.server.domain.chat.message.QChatMessage.chatMessage;
import static com.presto.server.domain.member.QMember.member;

import com.presto.server.domain.CursorPageInfo;
import com.presto.server.domain.CursorPaginatedResponse;
import com.presto.server.domain.chat.message.ChatMessageRepositoryCustom;
import com.presto.server.domain.chat.message.MessageCursorDirection;
import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import com.presto.server.domain.chat.message.dto.ChatMessagesQuery;
import com.presto.server.domain.chat.message.dto.QChatMessageDto;
import com.presto.server.domain.chat.message.dto.QChatMessageDto_Sender;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public CursorPaginatedResponse<ChatMessageDto> findChatMessages(ChatMessagesQuery query) {
        List<ChatMessageDto> chatMessages = queryFactory
                .select(new QChatMessageDto(
                        chatMessage.id,
                        chatMessage.content,
                        chatMessage.type,
                        new QChatMessageDto_Sender(
                                member.id,
                                member.username
                        ),
                        chatMessage.createdAt
                ))
                .from(chatMessage)
                .leftJoin(member).on(chatMessage.senderId.eq(member.id))
                .where(buildWhereClause(query))
                .orderBy(determineOrder(query))
                .limit(query.size() + 1L)
                .fetch();

        boolean hasNextPage = chatMessages.size() > query.size();
        boolean hasPreviousPage = !chatMessages.isEmpty() && query.direction() == MessageCursorDirection.PREV;

        if (hasNextPage) {
            chatMessages.removeLast();
        }

        if (hasPreviousPage) {
            chatMessages.removeFirst();
        }

        CursorPageInfo pageInfo = new CursorPageInfo(hasNextPage, hasPreviousPage);
        return new CursorPaginatedResponse<>(pageInfo, chatMessages);
    }

    private BooleanExpression buildWhereClause(ChatMessagesQuery query) {
        return chatMessage.chatRoomId.eq(query.chatRoomId())
                .and(buildMessageIdCondition(query));
    }

    private BooleanExpression buildMessageIdCondition(ChatMessagesQuery query) {
        if (query.messageId() == null) {
            return null;
        }
        return query.direction() == MessageCursorDirection.NEXT
                ? chatMessage.id.gt(query.messageId())
                : chatMessage.id.lt(query.messageId());
    }

    private OrderSpecifier<?> determineOrder(ChatMessagesQuery query) {
        return query.direction() == MessageCursorDirection.NEXT
                ? chatMessage.id.asc()
                : chatMessage.id.desc();
    }
}
