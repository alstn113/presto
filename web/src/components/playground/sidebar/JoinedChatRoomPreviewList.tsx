import { useEffect, useState } from 'react';
import useGetJoinedChatRoomPreviews from '../../../hooks/chat/useGetJoinedChatRoomPreviews';
import type { JoinedChatRoomPreviewResponse } from '../../../libs/api/chatRoomApi';
import JoinedChatRoomPreviewItem from './JoinedChatRoomPreviewItem';
import { SseEvent } from '../../../hooks/sse/sseEventType';
import type { JoinedChatRoomPreviewUpdatedEvent } from '../../../hooks/socket/types';
import useSseListener from '../../../hooks/sse/useSseListener';

const JoinedChatRoomPreviewList = () => {
  const { data: initialRooms } = useGetJoinedChatRoomPreviews();
  const [rooms, setRooms] = useState<JoinedChatRoomPreviewResponse[]>([]);

  const handleOnMessageReceived = (
    message: JoinedChatRoomPreviewUpdatedEvent
  ) => {
    setRooms((prevRooms) => {
      const updatedRooms = prevRooms.filter(
        (room) => room.chatRoomId !== message.chatRoomId
      );
      updatedRooms.push(message);
      return updatedRooms.sort(sortByLastMessageSentAt);
    });
  };

  const sortByLastMessageSentAt = (
    a: JoinedChatRoomPreviewResponse,
    b: JoinedChatRoomPreviewResponse
  ) => {
    return (
      new Date(b.lastMessageSentAt).getTime() -
      new Date(a.lastMessageSentAt).getTime()
    );
  };

  useEffect(() => {
    setRooms(initialRooms);
  }, [initialRooms]);

  useSseListener(
    SseEvent.JOINED_CHAT_ROOMS_PREVIEW_UPDATED,
    handleOnMessageReceived
  );

  return (
    <div className="mb-6">
      <h3 className="text-lg font-semibold text-gray-600 mb-2">참여 중</h3>
      <ul className="space-y-2">
        {rooms.length > 0 ? (
          rooms.map((room) => (
            <JoinedChatRoomPreviewItem key={room.chatRoomId} room={room} />
          ))
        ) : (
          <p className="text-sm text-gray-400">표시할 채팅방이 없습니다.</p>
        )}
      </ul>
    </div>
  );
};

export default JoinedChatRoomPreviewList;
