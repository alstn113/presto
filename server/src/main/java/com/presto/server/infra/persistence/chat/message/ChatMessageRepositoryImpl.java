package com.presto.server.infra.persistence.chat.message;

import static com.presto.server.domain.chat.message.QChatMessage.chatMessage;
import static com.presto.server.domain.member.QMember.member;

import com.presto.server.domain.CursorResult;
import com.presto.server.domain.chat.message.ChatMessageRepositoryCustom;
import com.presto.server.domain.chat.message.MessageCursorDirection;
import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import com.presto.server.domain.chat.message.dto.ChatMessagesQuery;
import com.presto.server.domain.chat.message.dto.QChatMessageDto;
import com.presto.server.domain.chat.message.dto.QChatMessageDto_Sender;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // - INIT: 초기 로드 시 lastMessageId가 null이면 제일 최신 메세지를 size 만큼 오름차순하여 가져온다.
    // - INIT: 초기 로드 시 lastMessageId가 null이 아니면 lastMessageId를 포함하지 않은 이전 메세지 size 만큼과
    //   lastMessageId를 포함한 이후 메세지 size 만큼을 오름차순 하여 가져온다.
    // - PREV: cursorMessageId를 포함한 이전 메세지를 size 만큼 오름차순 하여 가져온다.
    // - NEXT: cursorMessageId를 포함한 이후 메세지를 size 만큼 오름차순 하여 가져온다.
    @Override
    public CursorResult<ChatMessageDto> findChatMessages(ChatMessagesQuery query) {
        return switch (query.direction()) {
            case INIT -> handleInit(query);
            case PREV -> handlePrev(query);
            case NEXT -> handleNext(query);
        };
    }

    private CursorResult<ChatMessageDto> handleInit(ChatMessagesQuery query) {
        if (query.lastMessageId() == null) {
            List<ChatMessageDto> messages = fetchMessages(
                    query.chatRoomId(),
                    null,
                    null,
                    MessageCursorDirection.INIT,
                    query.size()
            );
            String prevCursor = extractCursor(messages, query.size());
            messages.sort(Comparator.comparing(ChatMessageDto::id));
            return new CursorResult<>(messages, prevCursor, null);
        }

        List<ChatMessageDto> prevMessages = fetchMessages(
                query.chatRoomId(),
                query.lastMessageId(),
                null,
                MessageCursorDirection.INIT,
                query.size()
        );
        String prevCursor = extractCursor(prevMessages, query.size());

        List<ChatMessageDto> nextAndCurMessages = fetchMessages(
                query.chatRoomId(),
                query.lastMessageId(),
                query.lastMessageId(), // cursorMessageId에 lastMessageId를 넣어준다.
                MessageCursorDirection.NEXT,
                query.size()
        );
        String nextCursor = extractCursor(nextAndCurMessages, query.size());

        List<ChatMessageDto> messages = new ArrayList<>(prevMessages);
        messages.addAll(nextAndCurMessages);
        messages.sort(Comparator.comparing(ChatMessageDto::id));

        return new CursorResult<>(messages, prevCursor, nextCursor);
    }

    private CursorResult<ChatMessageDto> handlePrev(ChatMessagesQuery query) {
        List<ChatMessageDto> messages = fetchMessages(
                query.chatRoomId(),
                query.lastMessageId(),
                query.cursorMessageId(),
                MessageCursorDirection.PREV,
                query.size()
        );
        String prevCursor = extractCursor(messages, query.size());
        messages.sort(Comparator.comparing(ChatMessageDto::id));

        return new CursorResult<>(messages, prevCursor, null);
    }

    private CursorResult<ChatMessageDto> handleNext(ChatMessagesQuery query) {
        List<ChatMessageDto> messages = fetchMessages(
                query.chatRoomId(),
                query.lastMessageId(),
                query.cursorMessageId(),
                MessageCursorDirection.NEXT,
                query.size()
        );
        String nextCursor = extractCursor(messages, query.size());
        messages.sort(Comparator.comparing(ChatMessageDto::id));

        return new CursorResult<>(messages, null, nextCursor);
    }

    private List<ChatMessageDto> fetchMessages(
            String chatRoomId,
            String lastMessageId,
            String cursorMessageId,
            MessageCursorDirection direction,
            int size
    ) {
        return queryFactory
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
                .where(chatMessage.chatRoomId.eq(chatRoomId)
                        .and(buildCursorCondition(direction, lastMessageId, cursorMessageId)))
                .orderBy(orderBy(direction))
                .limit(size + 1L)
                .fetch();
    }

    private BooleanExpression buildCursorCondition(
            MessageCursorDirection direction,
            String lastMessageId,
            String cursorMessageId
    ) {
        return switch (direction) {
            case INIT -> {
                if (lastMessageId == null) {
                    yield null;
                }
                yield chatMessage.id.lt(lastMessageId);
            }
            case PREV -> chatMessage.id.loe(cursorMessageId);
            case NEXT -> chatMessage.id.goe(cursorMessageId);
        };
    }

    private OrderSpecifier<String> orderBy(MessageCursorDirection direction) {
        return switch (direction) {
            case INIT, PREV -> chatMessage.id.desc();
            case NEXT -> chatMessage.id.asc();
        };
    }

    private String extractCursor(List<ChatMessageDto> messages, int size) {
        // PREV의 경우, 내림차순(이후 -> 이전)으로 가져와서 마지막 값을 지우고 Cursor로 쓴다.
        // NEXT의 경우, 오름차순(이전 -> 이후)으로 가져와서 마지막 값을 지우고 Cursor로 쓴다.
        if (messages.size() > size) {
            return messages.removeLast().id();
        }
        return null;
    }
}
