package com.presto.server.infra.persistence.chat.message;

import static com.presto.server.domain.chat.message.QChatMessage.chatMessage;
import static com.presto.server.domain.member.QMember.member;

import com.presto.server.domain.chat.message.ChatMessageRepositoryCustom;
import com.presto.server.domain.chat.message.MessageCursorDirection;
import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import com.presto.server.domain.chat.message.dto.ChatMessagesCursorResponse;
import com.presto.server.domain.chat.message.dto.QChatMessageDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public ChatMessagesCursorResponse findChatMessagesCursor(
            String chatRoomId,
            String cursorMessageId,
            MessageCursorDirection direction,
            int size
    ) {
        List<ChatMessageDto> messages = queryFactory
                .select(new QChatMessageDto(
                        chatMessage.id,
                        chatMessage.content,
                        chatMessage.type,
                        chatMessage.senderId,
                        chatMessage.createdAt
                ))
                .from(chatMessage)
                .leftJoin(member).on(chatMessage.senderId.eq(member.id))
                .where(chatMessage.chatRoomId.eq(chatRoomId)
                        .and(buildCursorCondition(direction, cursorMessageId)))
                .orderBy(orderBy(direction))
                .limit(size + 1L)
                .fetch();

        String cursor = extractCursor(messages, size);
        messages.sort(Comparator.comparing(ChatMessageDto::id));

        return new ChatMessagesCursorResponse(
                messages,
                direction.isPrev() ? cursor : null,
                direction.isNext() ? cursor : null
        );
    }

    private BooleanExpression buildCursorCondition(MessageCursorDirection direction, String cursorMessageId) {
        if (cursorMessageId == null) {
            return null;
        }

        return switch (direction) {
            case NEXT -> chatMessage.id.goe(cursorMessageId);
            case PREV -> chatMessage.id.loe(cursorMessageId);
        };
    }

    private OrderSpecifier<String> orderBy(MessageCursorDirection direction) {
        return switch (direction) {
            case NEXT -> chatMessage.id.asc();
            case PREV -> chatMessage.id.desc();
        };
    }

    private String extractCursor(List<ChatMessageDto> messages, int size) {
        if (messages.size() > size) {
            return messages.removeLast().id();
        }
        return null;
    }
}
