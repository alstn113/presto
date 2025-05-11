package com.presto.server.application.chat.message;

import com.presto.server.application.chat.message.request.ChatMessageRequest;
import com.presto.server.application.chat.message.response.ChatMessageReceivedEvent;
import com.presto.server.application.chat.message.response.ChatMessageReceivedEvent.Sender;
import com.presto.server.domain.chat.message.ChatMessage;
import com.presto.server.domain.chat.message.ChatMessageRepository;
import com.presto.server.domain.chat.message.MessageType;
import com.presto.server.domain.chat.room.ChatRoom;
import com.presto.server.domain.chat.room.ChatRoomRepository;
import com.presto.server.domain.member.Member;
import com.presto.server.domain.member.MemberRepository;
import com.presto.server.support.error.CoreException;
import com.presto.server.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(ChatMessageRequest request) {
        Member member = findMemberById(request.accessor().id());
        ChatRoom chatRoom = findChatRoomById(request.chatRoomId());

        ChatMessage chatMessage = createChatMessage(chatRoom, member, request);
        chatMessageRepository.save(chatMessage);

        ChatMessageReceivedEvent event = createChatMessageReceivedEvent(chatMessage, member);
        String destination = "/topic/chat/%d".formatted(chatRoom.getId());
        messagingTemplate.convertAndSend(destination, event);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CoreException(ErrorType.MEMBER_NOT_FOUND));
    }

    private ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CoreException(ErrorType.CHAT_ROOM_NOT_FOUND));
    }

    private ChatMessage createChatMessage(ChatRoom chatRoom, Member member, ChatMessageRequest request) {
        return new ChatMessage(
                chatRoom.getId(),
                member.getId(),
                MessageType.TEXT,
                request.content()
        );
    }

    private ChatMessageReceivedEvent createChatMessageReceivedEvent(ChatMessage chatMessage, Member member) {
        return new ChatMessageReceivedEvent(
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getType(),
                new Sender(member.getId(), member.getUsername()),
                chatMessage.getCreatedAt()
        );
    }
}

