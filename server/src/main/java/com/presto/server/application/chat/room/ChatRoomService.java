package com.presto.server.application.chat.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    public void createChatRoom(String roomName, String roomType) {
        // Logic to create a chat room
        // This could involve saving the room details to a database
        // and notifying users about the new room.
    }

}
