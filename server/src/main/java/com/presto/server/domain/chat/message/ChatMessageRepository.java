package com.presto.server.domain.chat.message;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String>, ChatMessageRepositoryCustom {
}
