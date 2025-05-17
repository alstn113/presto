package com.presto.server.infra.persistence.chat.message;

import static com.presto.server.domain.chat.message.QChatMessage.chatMessage;
import static com.presto.server.domain.chat.room.QChatRoomParticipant.chatRoomParticipant;
import static com.presto.server.domain.member.QMember.member;

import com.presto.server.domain.CursorResult;
import com.presto.server.domain.chat.message.ChatMessageRepositoryCustom;
import com.presto.server.domain.chat.message.MessageCursorDirection;
import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import com.presto.server.domain.chat.message.dto.ChatMessagesRequest;
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

    // - INIT: 초기 로드 시 lastReadMessageId가 null이면 제일 최신 메세지를 size 만큼 오름차순하여 가져온다.
    // - INIT: 초기 로드 시 lastReadMessageId가 null이 아니면 lastReadMessageId를 포함하지 않은 이전 메세지 size 만큼과
    //   lastReadMessageId를 포함한 이후 메세지 size 만큼을 오름차순 하여 가져온다.
    // - PREV: cursorMessageId를 포함한 이전 메세지를 size 만큼 오름차순 하여 가져온다.
    // - NEXT: cursorMessageId를 포함한 이후 메세지를 size 만큼 오름차순 하여 가져온다.
    @Override
    public CursorResult<ChatMessageDto> findChatMessages(ChatMessagesRequest request) {
        return switch (request.direction()) {
            case INIT -> handleInit(request);
            case PREV -> handlePrev(request);
            case NEXT -> handleNext(request);
        };
    }

    private CursorResult<ChatMessageDto> handleInit(ChatMessagesRequest request) {
        String lastReadMessageId = getLastReadMessageId(request);

        if (lastReadMessageId == null) {
            List<ChatMessageDto> messages = fetchMessages(
                    request.chatRoomId(),
                    null,
                    null,
                    MessageCursorDirection.INIT,
                    request.size()
            );
            String prevCursor = extractCursor(messages, request.size());
            messages.sort(Comparator.comparing(ChatMessageDto::id));
            return new CursorResult<>(messages, prevCursor, null, null);
        }

        List<ChatMessageDto> prevMessages = fetchMessages(
                request.chatRoomId(),
                lastReadMessageId,
                null,
                MessageCursorDirection.INIT,
                request.size()
        );
        String prevCursor = extractCursor(prevMessages, request.size());

        List<ChatMessageDto> nextAndCurMessages = fetchMessages(
                request.chatRoomId(),
                lastReadMessageId,
                lastReadMessageId,
                MessageCursorDirection.NEXT,
                request.size()
        );
        String nextCursor = extractCursor(nextAndCurMessages, request.size());

        List<ChatMessageDto> messages = new ArrayList<>(prevMessages);
        messages.addAll(nextAndCurMessages);
        messages.sort(Comparator.comparing(ChatMessageDto::id));

        return new CursorResult<>(messages, prevCursor, nextCursor, lastReadMessageId);
    }

    private String getLastReadMessageId(ChatMessagesRequest request) {
        return queryFactory
                .select(chatRoomParticipant.lastReadMessageId)
                .from(chatRoomParticipant)
                .where(chatRoomParticipant.chatRoomId.eq(request.chatRoomId())
                        .and(chatRoomParticipant.memberId.eq(request.memberId())))
                .fetchOne();
    }

    private CursorResult<ChatMessageDto> handlePrev(ChatMessagesRequest request) {
        List<ChatMessageDto> messages = fetchMessages(
                request.chatRoomId(),
                null,
                request.cursorMessageId(),
                MessageCursorDirection.PREV,
                request.size()
        );
        String prevCursor = extractCursor(messages, request.size());
        messages.sort(Comparator.comparing(ChatMessageDto::id));

        return new CursorResult<>(messages, prevCursor, null, null);
    }

    private CursorResult<ChatMessageDto> handleNext(ChatMessagesRequest request) {
        List<ChatMessageDto> messages = fetchMessages(
                request.chatRoomId(),
                null,
                request.cursorMessageId(),
                MessageCursorDirection.NEXT,
                request.size()
        );
        String nextCursor = extractCursor(messages, request.size());
        messages.sort(Comparator.comparing(ChatMessageDto::id));

        return new CursorResult<>(messages, null, nextCursor, null);
    }

    private List<ChatMessageDto> fetchMessages(
            String chatRoomId,
            String lastReadMessageId,
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
                        .and(buildCursorCondition(direction, lastReadMessageId, cursorMessageId)))
                .orderBy(orderBy(direction))
                .limit(size + 1L)
                .fetch();
    }

    private BooleanExpression buildCursorCondition(
            MessageCursorDirection direction,
            String lastReadMessageId,
            String cursorMessageId
    ) {
        return switch (direction) {
            case INIT -> {
                if (lastReadMessageId == null) {
                    yield null;
                }
                yield chatMessage.id.lt(lastReadMessageId);
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
