package com.presto.server.domain.chat.room;

import com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.MyChatRoomPreviewDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
            select new com.presto.server.domain.chat.room.dto.MyChatRoomPreviewDto(
                cr.id,
                cr.name,
                cm.content,
                cm.createdAt
            )
            from ChatRoomParticipant crp
            join ChatRoom cr on crp.chatRoomId = cr.id
            left join ChatMessage cm on cr.id = cm.chatRoomId
            where crp.memberId = :memberId
              and cm.createdAt = (
                  select max(sub.createdAt)
                  from ChatMessage sub
                  where sub.chatRoomId = cr.id
              )
            order by cm.createdAt desc nulls last
            """)
    List<MyChatRoomPreviewDto> findMyChatRoomPreviews(Long memberId);

    @Query("""
            select new com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto(
                cr.id,
                cr.name,
                cr.createdAt
            )
            from ChatRoom cr
            left join ChatRoomParticipant cp on cr.id = cp.chatRoomId and cp.memberId = :id
            where cp.memberId is null
            """)
    List<AvailableChatRoomPreviewDto> findAvailableChatRoomPreviews(Long memberId);
}
