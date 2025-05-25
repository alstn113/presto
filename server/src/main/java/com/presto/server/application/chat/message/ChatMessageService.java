package com.presto.server.application.chat.message;

import com.presto.server.application.chat.message.request.ChatMessageRequest;
import com.presto.server.application.chat.message.response.ChatMessageReceivedEvent;
import com.presto.server.application.sse.SseEmitterService;
import com.presto.server.domain.chat.message.ChatMessage;
import com.presto.server.domain.chat.message.ChatMessageRepository;
import com.presto.server.domain.chat.message.MessageType;
import com.presto.server.domain.chat.room.ChatRoom;
import com.presto.server.domain.chat.room.ChatRoomParticipant;
import com.presto.server.domain.chat.room.ChatRoomParticipantRepository;
import com.presto.server.domain.chat.room.ChatRoomRepository;
import com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto;
import com.presto.server.support.error.CoreException;
import com.presto.server.support.error.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final SseEmitterService sseEmitterService;

    @Transactional
    public void sendMessage(ChatMessageRequest request) {
        String senderId = request.accessor().id();
        ChatRoom chatRoom = findChatRoomById(request.chatRoomId());

        ChatMessage chatMessage = new ChatMessage(
                chatRoom.getId(),
                senderId,
                MessageType.TEXT,
                request.content()
        );
        chatMessageRepository.save(chatMessage);

        sendChatMessageReceivedEvent(chatRoom, chatMessage, senderId);
        sendChatRoomPreviewUpdatedEvent(chatRoom.getId());
    }

    private void sendChatMessageReceivedEvent(ChatRoom chatRoom, ChatMessage chatMessage, String senderId) {
        ChatMessageReceivedEvent event = new ChatMessageReceivedEvent(
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getType(),
                senderId,
                chatMessage.getCreatedAt()
        );
        String destination = "/topic/chat/%s".formatted(chatRoom.getId());
        messagingTemplate.convertAndSend(destination, event);
    }

    private void sendChatRoomPreviewUpdatedEvent(String chatRoomId) {
        List<ChatRoomParticipant> participants = chatRoomParticipantRepository.findByChatRoomId(chatRoomId);
        participants.forEach(it -> {
            String memberId = it.getMemberId();
            JoinedChatRoomPreviewDto event = chatRoomRepository
                    .findJoinedChatRoomPreviewByMemberId(chatRoomId, memberId)
                    .orElseThrow(() -> new CoreException(ErrorType.CHAT_ROOM_NOT_FOUND));
            sseEmitterService.sendChatRoomPreviewUpdatedEvent(memberId, event);
        });
    }

    private ChatRoom findChatRoomById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CoreException(ErrorType.CHAT_ROOM_NOT_FOUND));
    }
}

