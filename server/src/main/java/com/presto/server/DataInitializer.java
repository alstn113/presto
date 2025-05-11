package com.presto.server;

import com.presto.server.application.auth.AuthService;
import com.presto.server.application.auth.request.RegisterRequest;
import com.presto.server.domain.chat.room.ChatRoom;
import com.presto.server.domain.chat.room.ChatRoomRepository;
import com.presto.server.domain.chat.room.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final AuthService authService;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @jakarta.transaction.Transactional
    public void run(ApplicationArguments args) {
        Long catMemberId = authService.register(new RegisterRequest("cat", "123"));
        Long dogMemberId = authService.register(new RegisterRequest("dog", "123"));

        chatRoomRepository.save(new ChatRoom("대화방", RoomType.PUBLIC, catMemberId));
        chatRoomRepository.save(new ChatRoom("공지사항", RoomType.PUBLIC, catMemberId));
        chatRoomRepository.save(new ChatRoom("자유게시판", RoomType.PUBLIC, catMemberId));
        chatRoomRepository.save(new ChatRoom("질문과답변", RoomType.PUBLIC, catMemberId));
        chatRoomRepository.save(new ChatRoom("건의사항", RoomType.PUBLIC, catMemberId));
        chatRoomRepository.save(new ChatRoom("질문", RoomType.PUBLIC, dogMemberId));
        chatRoomRepository.save(new ChatRoom("발표자료", RoomType.PUBLIC, dogMemberId));
    }
}
