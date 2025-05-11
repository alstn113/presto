package com.presto.server.application.chat.room;

import com.presto.server.application.chat.room.request.AvailableChatRoomPreviewsQuery;
import com.presto.server.application.chat.room.request.MyChatRoomPreviewsQuery;
import com.presto.server.domain.chat.room.dto.AvailableChatRoomPreviewDto;
import com.presto.server.domain.chat.room.dto.MyChatRoomPreviewDto;
import com.presto.server.domain.chat.room.ChatRoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public List<MyChatRoomPreviewDto> getMyChatRoomPreviews(MyChatRoomPreviewsQuery query) {
        Long memberId = query.accessor().id();
        return chatRoomRepository.findMyChatRoomPreviews(memberId);
    }

    @Transactional(readOnly = true)
    public List<AvailableChatRoomPreviewDto> getAvailableChatRoomPreviews(AvailableChatRoomPreviewsQuery query) {
        Long memberId = query.accessor().id();
        return chatRoomRepository.findAvailableChatRoomPreviews(memberId);
    }
}
