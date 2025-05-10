package com.presto.server.domain.chat.room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {
}
