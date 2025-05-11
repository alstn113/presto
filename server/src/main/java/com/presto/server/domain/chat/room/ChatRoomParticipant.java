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
    private Long chatRoomId;

    @Column(nullable = false)
    private Long memberId;

    @Column
    private Long lastReadMessageId;

    public ChatRoomParticipant(Long chatRoomId, Long memberId) {
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.lastReadMessageId = null;
    }
}
