package com.presto.server.domain.chat.room;

import com.presto.server.infra.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomParticipant extends BaseEntity {

    @Column(nullable = false)
    private Long chatRoomId;

    @Column(nullable = false)
    private Long memberId;

    @Column
    private Long lastReadMessageId;
}
