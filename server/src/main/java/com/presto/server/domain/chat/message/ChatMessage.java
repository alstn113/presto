package com.presto.server.domain.chat.message;

import com.presto.server.infra.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatMessage extends BaseEntity {

    @Column(nullable = false)
    private String chatRoomId;

    @Column(nullable = false)
    private String senderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(nullable = false)
    private String content;

    public ChatMessage(
            String chatRoomId,
            String senderId,
            MessageType type,
            String content
    ) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.type = type;
        this.content = content;
    }
}
