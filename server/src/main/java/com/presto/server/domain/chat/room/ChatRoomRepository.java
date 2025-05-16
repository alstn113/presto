package com.presto.server.domain.chat.room;

import com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("""
                select new com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto(
                    cr.id,
                    cr.name,
                    cmLatest.content,
                    cmLatest.createdAt,
                    count(distinct unreadMsg),
                    count(distinct roomPart)
                )
                from ChatRoomParticipant userPart
                join ChatRoom cr on userPart.chatRoomId = cr.id and userPart.memberId = :memberId
                left join ChatMessage cmLatest on cmLatest.chatRoomId = cr.id and cmLatest.createdAt = (
                    select max(cm2.createdAt)
                    from ChatMessage cm2
                    where cm2.chatRoomId = cr.id
                )
                left join ChatMessage unreadMsg on unreadMsg.chatRoomId = cr.id and (userPart.lastReadMessageId is null or unreadMsg.id > userPart.lastReadMessageId)
                left join ChatRoomParticipant roomPart on roomPart.chatRoomId = cr.id
                group by cr.id, cr.name, cmLatest.content, cmLatest.createdAt
                order by cmLatest.createdAt desc nulls last
            """)
    List<JoinedChatRoomPreviewDto> findJoinedChatRoomPreviews(String memberId);

    @Query("""
                select new com.presto.server.domain.chat.room.dto.JoinedChatRoomPreviewDto(
                    cr.id,
                    cr.name,
                    cmLatest.content,
                    cmLatest.createdAt,
                    count(distinct unreadMsg),
                    count(distinct roomPart)
                )
                from ChatRoomParticipant userPart
                join ChatRoom cr on userPart.chatRoomId = cr.id and userPart.memberId = :memberId
                left join ChatMessage cmLatest on cmLatest.chatRoomId = cr.id and cmLatest.createdAt = (
                    select max(cm2.createdAt)
                    from ChatMessage cm2
                    where cm2.chatRoomId = cr.id
                )
                left join ChatMessage unreadMsg on unreadMsg.chatRoomId = cr.id and (userPart.lastReadMessageId is null or unreadMsg.id > userPart.lastReadMessageId)
                left join ChatRoomParticipant roomPart on roomPart.chatRoomId = cr.id
                where cr.id = :chatRoomId
                group by cr.id, cr.name, cmLatest.content, cmLatest.createdAt
            """)
    Optional<JoinedChatRoomPreviewDto> findJoinedChatRoomPreviewByMemberId(String chatRoomId, String memberId);

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
