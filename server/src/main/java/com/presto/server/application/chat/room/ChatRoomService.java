package com.presto.server.application.chat.room;

import com.presto.server.application.chat.room.request.JoinChatRoomRequest;
import com.presto.server.application.chat.room.request.LeaveChatRoomRequest;
import com.presto.server.domain.chat.room.ChatRoom;
import com.presto.server.domain.chat.room.ChatRoomParticipant;
import com.presto.server.domain.chat.room.ChatRoomParticipantRepository;
import com.presto.server.domain.chat.room.ChatRoomRepository;
import com.presto.server.support.error.CoreException;
import com.presto.server.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Transactional
    public void joinChatRoom(JoinChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.chatRoomId())
                .orElseThrow(() -> new CoreException(ErrorType.CHAT_ROOM_NOT_FOUND));
        ChatRoomParticipant participant = new ChatRoomParticipant(chatRoom.getId(), request.accessor().id());

        chatRoomParticipantRepository.save(participant);
    }

    @Transactional
    public void leaveChatRoom(LeaveChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.chatRoomId())
                .orElseThrow(() -> new CoreException(ErrorType.CHAT_ROOM_NOT_FOUND));
        ChatRoomParticipant participant = chatRoomParticipantRepository
                .findByChatRoomIdAndMemberId(chatRoom.getId(), request.accessor().id())
                .orElseThrow(() -> new CoreException(ErrorType.CHAT_ROOM_PARTICIPANT_NOT_FOUND));

        chatRoomParticipantRepository.delete(participant);
    }
}

