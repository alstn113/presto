package com.presto.server.domain.chat.room;

import com.presto.server.infra.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"chat_room_id", "member_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomParticipant extends BaseEntity {

    @Column(nullable = false)
    private String chatRoomId;

    @Column(nullable = false)
    private String memberId;

    @Column
    private String lastReadMessageId;

    public ChatRoomParticipant(String chatRoomId, String memberId) {
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.lastReadMessageId = null;
    }
}
