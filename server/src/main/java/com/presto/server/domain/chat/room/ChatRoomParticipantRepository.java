package com.presto.server.domain.chat.room;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, String> {

    Optional<ChatRoomParticipant> findByChatRoomIdAndMemberId(String chatRoomId, String memberId);

    List<ChatRoomParticipant> findByChatRoomId(String chatRoomId);
}
