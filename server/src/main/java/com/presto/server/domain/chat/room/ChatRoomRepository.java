package com.presto.server.domain.chat.room;

import com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("""
            SELECT new com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto(
                cr.id,
                cr.name,
                cm.content,
                cm.createdAt
            )
            FROM ChatRoomParticipant crp
            JOIN ChatRoom cr ON crp.chatRoomId = cr.id
            LEFT JOIN ChatMessage cm ON cm.chatRoomId = cr.id AND cm.createdAt = (
                SELECT MAX(cm2.createdAt)
                FROM ChatMessage cm2
                WHERE cm2.chatRoomId = cr.id
            )
            WHERE crp.memberId = :memberId
            ORDER BY cm.createdAt DESC NULLS LAST
            """)
    List<JoinedChatRoomPreviewDto> findJoinedChatRoomPreviews(String memberId);

    @Query("""
            select new com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto(
                cr.id,
                cr.name,
                cr.createdAt
            )
            from ChatRoom cr
            left join ChatRoomParticipant cp on cr.id = cp.chatRoomId and cp.memberId = :memberId
            where cp.memberId is null
            """)
    List<AvailableChatRoomPreviewDto> findAvailableChatRoomPreviews(String memberId);
}
