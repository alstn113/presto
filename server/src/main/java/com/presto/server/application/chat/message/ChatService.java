package com.presto.server.application.chat.message;

import com.presto.server.application.chat.message.request.ChatMessageRequest;
import com.presto.server.application.chat.message.response.ChatMessageReceivedEvent;
import com.presto.server.application.chat.message.response.ChatMessageReceivedEvent.Sender;
import com.presto.server.application.chat.message.response.JoinedChatRoomPreviewUpdatedEvent;
import com.presto.server.domain.chat.message.ChatMessage;
import com.presto.server.domain.chat.message.ChatMessageRepository;
import com.presto.server.domain.chat.message.MessageType;
import com.presto.server.domain.chat.room.ChatRoom;
import com.presto.server.domain.chat.room.ChatRoomParticipant;
import com.presto.server.domain.chat.room.ChatRoomParticipantRepository;
import com.presto.server.domain.chat.room.ChatRoomRepository;
import com.presto.server.domain.member.Member;
import com.presto.server.domain.member.MemberRepository;
import com.presto.server.support.error.CoreException;
import com.presto.server.support.error.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendMessage(ChatMessageRequest request) {
        Member member = findMemberById(request.accessor().id());
        ChatRoom chatRoom = findChatRoomById(request.chatRoomId());

        ChatMessage chatMessage = new ChatMessage(
                chatRoom.getId(),
                member.getId(),
                MessageType.TEXT,
                request.content()
        );
        chatMessageRepository.save(chatMessage);

        sendChatMessageReceivedEvent(chatRoom, chatMessage, member);
        sendChatRoomPreviewUpdatedEvent(chatRoom, chatMessage);
    }

    private void sendChatMessageReceivedEvent(ChatRoom chatRoom, ChatMessage chatMessage, Member member) {
        ChatMessageReceivedEvent event = new ChatMessageReceivedEvent(
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getType(),
                new Sender(member.getId(), member.getUsername()),
                chatMessage.getCreatedAt()
        );
        String destination = "/topic/chat/%s".formatted(chatRoom.getId());
        messagingTemplate.convertAndSend(destination, event);
    }

    private void sendChatRoomPreviewUpdatedEvent(ChatRoom chatRoom, ChatMessage chatMessage) {
        List<ChatRoomParticipant> participants = chatRoomParticipantRepository.findByChatRoomId(chatRoom.getId());
        JoinedChatRoomPreviewUpdatedEvent event = new JoinedChatRoomPreviewUpdatedEvent(
                chatRoom.getId(),
                chatRoom.getName(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt()
        );
        participants.forEach(it -> {
            String destination = "/queue/chat-rooms/joined";
            messagingTemplate.convertAndSendToUser(it.getMemberId(), destination, event);
        });
    }

    private Member findMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CoreException(ErrorType.MEMBER_NOT_FOUND));
    }

    private ChatRoom findChatRoomById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CoreException(ErrorType.CHAT_ROOM_NOT_FOUND));
    }
}

