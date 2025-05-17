package com.presto.server.application.chat.room;

import com.presto.server.api.chat.room.request.ChatRoomOverviewRequest;
import com.presto.server.application.chat.room.request.AvailableChatRoomPreviewsQuery;
import com.presto.server.application.chat.room.request.JoinedChatRoomPreviewsQuery;
import com.presto.server.domain.chat.message.ChatMessageRepository;
import com.presto.server.domain.chat.message.MessageCursorDirection;
import com.presto.server.domain.chat.message.dto.ChatMessageDto;
import com.presto.server.domain.chat.message.dto.ChatMessagesCursorResponse;
import com.presto.server.domain.chat.room.ChatRoomRepository;
import com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.ChatParticipantInfoDto;
import com.presto.server.domain.chat.room.dto.ChatRoomOverviewResponse;
import com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional(readOnly = true)
    public List<JoinedChatRoomPreviewDto> getJoinedChatRoomPreviews(JoinedChatRoomPreviewsQuery query) {
        return chatRoomRepository.findJoinedChatRoomPreviews(query.accessor().id());
    }

    @Transactional(readOnly = true)
    public List<AvailableChatRoomPreviewDto> getAvailableChatRoomPreviews(AvailableChatRoomPreviewsQuery query) {
        return chatRoomRepository.findAvailableChatRoomPreviews(query.accessor().id());
    }

    @Transactional(readOnly = true)
    public ChatRoomOverviewResponse getChatRoomOverview(ChatRoomOverviewRequest request) {
        String chatRoomId = request.chatRoomId();
        String memberId = request.memberId();
        int size = request.size();

        List<ChatParticipantInfoDto> participants = chatRoomRepository.findChatParticipantInfos(chatRoomId);
        String lastReadMessageId = findLastReadMessageId(participants, memberId);

        // 최신 메세지를 size만큼 (이전 -> 이후)로 가져온다.
        if (lastReadMessageId == null) {
            return buildInitialOverview(chatRoomId, participants, memberId, size);
        }

        // lastReadMessageId를 포함한 이전 메세지 size개와 이후 메세지 size개를 (이전 -> 이후)로 가져온다.
        return buildOverviewAroundMessage(chatRoomId, participants, memberId, lastReadMessageId, size);
    }

    private ChatRoomOverviewResponse buildInitialOverview(
            String chatRoomId,
            List<ChatParticipantInfoDto> participants,
            String memberId,
            int size
    ) {
        ChatMessagesCursorResponse response = chatMessageRepository
                .findChatMessagesCursor(chatRoomId, null, MessageCursorDirection.PREV, size);

        return new ChatRoomOverviewResponse(
                response.messages(),
                participants,
                memberId,
                response.prevCursor(),
                null
        );
    }

    private ChatRoomOverviewResponse buildOverviewAroundMessage(
            String chatRoomId,
            List<ChatParticipantInfoDto> participants,
            String memberId,
            String lastReadMessageId,
            int size
    ) {
        ChatMessagesCursorResponse prevMessages = chatMessageRepository
                .findChatMessagesCursor(chatRoomId, lastReadMessageId, MessageCursorDirection.PREV, size);
        ChatMessagesCursorResponse nextMessages = chatMessageRepository
                .findChatMessagesCursor(chatRoomId, lastReadMessageId, MessageCursorDirection.NEXT, size + 1);

        List<ChatMessageDto> next = nextMessages.messages();
        List<ChatMessageDto> nextWithoutCursor = next.size() > 1 ? next.subList(1, next.size()) : List.of();

        List<ChatMessageDto> allMessages = new ArrayList<>(prevMessages.messages());
        allMessages.addAll(nextWithoutCursor);

        return new ChatRoomOverviewResponse(
                allMessages,
                participants,
                memberId,
                prevMessages.prevCursor(),
                nextMessages.nextCursor()
        );
    }

    @Nullable
    private String findLastReadMessageId(List<ChatParticipantInfoDto> participants, String memberId) {
        for (ChatParticipantInfoDto participant : participants) {
            if (participant.id().equals(memberId)) {
                return participant.lastReadMessageId();
            }
        }

        return null;
    }
}
